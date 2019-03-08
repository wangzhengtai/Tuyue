package com.example.tuyue.util;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/*OkHttp网络请求封装类*/
public class OkHttpEngine {

    private static final String TAG = "OkHttpEngine";

    //回调接口
    public interface Callback{
        void onResponse(Response response) throws IOException;
        void onError(Request request,Exception e);
    }

    private static volatile OkHttpEngine mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;

    private OkHttpEngine(Context context){
        File cacheDir = new File(context.getCacheDir(),"OkHttp");
        if (!cacheDir.exists()){
            if (!cacheDir.mkdir()){
                throw new ExceptionInInitializerError("OkHttpEngine:can't mkdir the dir");
            }
        }
        int cacheSize = 500*1024*1024;         //500M 硬盘缓存
        Cache cache = new Cache(cacheDir.getAbsoluteFile(),cacheSize);
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20,TimeUnit.SECONDS)
                .addInterceptor(new ClientInterceptor(context))
                .addNetworkInterceptor(new NetworkInterceptor())
                .cache(cache);
        mOkHttpClient = builder.build();
        mHandler = new Handler();             //此方法最终会在主线程调用
    }

    //双重检查模式
    public static OkHttpEngine getInstance(Context context){
        if (mInstance == null){
            synchronized (OkHttpEngine.class){
                if (mInstance == null){
                    mInstance = new OkHttpEngine(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    public void getAsyncHttp(String url, okhttp3.Callback callback){
        Request request = new Request.Builder()
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
        //dealRequest(request,callback);
    }

    public void postAsyncHttp(String url, List<Pair<String,String>> pairs, okhttp3.Callback callback){
        FormBody.Builder builder = new FormBody.Builder();
        for (Pair<String,String> pair : pairs){
            builder.add(pair.first,pair.second);
        }
        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);
        //dealRequest(request,callback);
    }

    public void cancelAllCall(){
        mOkHttpClient.dispatcher().cancelAll();
    }

    //傻逼 再加载时，自然会再次请求
//    public void resumeAllCall(){
//        //得到所有call
//        List<Call> calls = mOkHttpClient.dispatcher().queuedCalls();
//        calls.addAll(mOkHttpClient.dispatcher().runningCalls());
//        for (Call call:calls){
//            call.enqueue();
//        }
//    }

//    public void postAsyncHttp(String url,int cid,int start,int count,CallBack callBack){
//        RequestBody requestBody = new FormBody.Builder()
//                .add("cid",String.valueOf(cid))
//                .add("start",String.valueOf(start))
//                .add("count",String.valueOf(count))
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
//        Call call = mOkHttpClient.newCall(request);
//        dealCall(call,callBack);
//    }

    private void dealRequest(Request request,Callback callback){
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                sendFailedCallback(call.request(),e,callback);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.body() != null) {
                    sendSuccessCallback(response,callback);
                }else{
                    Log.d(TAG, "onResponse: the response is null!");
                }
            }
        });
    }

    private void sendSuccessCallback(Response response,Callback callBack){
        mHandler.post(() -> {
            if (callBack != null){
                try {
                    callBack.onResponse(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendFailedCallback(Request request,Exception e,Callback callBack){
        mHandler.post(() -> {
            if (callBack != null){
                callBack.onError(request,e);
            }
        });
    }

    private static class ClientInterceptor implements okhttp3.Interceptor{

        private Context mContext;

        ClientInterceptor(Context context){
            mContext = context;
        }

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {

            if (NetworkUtil.isConnected(mContext)) {
                return chain.proceed(chain.request());
            } else { // 如果没有网络，则返回缓存未过期一个月的数据
                Request newRequest = chain.request().newBuilder()
                        .cacheControl(new CacheControl.Builder()
                        .onlyIfCached()
                        .maxStale(30,TimeUnit.DAYS)
                        .build())
                        //.removeHeader("Pragma")
                        //.header("Cache-Control", "only-if-cached, max-stale=" + 30 * 24 * 60 * 60)
                        .build();
                return chain.proceed(newRequest);
            }

        }
    }

    private static class NetworkInterceptor implements Interceptor {

        @NonNull
        @Override
        public Response intercept(@NonNull Chain chain) throws IOException {
            //添加Cache-Control，max-age为一天
            return chain.proceed(chain.request())
                    .newBuilder()
                    .removeHeader("Pragma")
                    .addHeader("Cache-Control","max-age="+24*60*60)
                    .build();
        }
    }
}
