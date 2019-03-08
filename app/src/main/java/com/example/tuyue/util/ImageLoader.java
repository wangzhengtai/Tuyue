package com.example.tuyue.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tuyue.database.PictureInfo;
import com.example.tuyue.database.PictureTable;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okio.ByteString;

public class ImageLoader {

    private static final String TAG = "ImageLoader";

    private static volatile ImageLoader mInstance;
    private LruCache<String, Bitmap> mMemoryCache;
    private OkHttpEngine mOkHttpEngine;
    private Handler mHandler;
    private PictureTable mPictureTable;

    private ImageLoader(Context context){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        int cacheSize = maxMemory/4;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(@NonNull String key, @NonNull Bitmap value) {
                return value.getRowBytes()*value.getHeight()/1024;
            }
        };
        mOkHttpEngine = OkHttpEngine.getInstance(context);
        mHandler = new Handler();       //这个会在主线程调用
        mPictureTable = new PictureTable(context);
    }

    public static ImageLoader getInstance(Context context){
        if (mInstance == null){
            synchronized (ImageLoader.class){
                if (mInstance == null){
                    mInstance = new ImageLoader(context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    //异步加载
    public void loadBitmap(String url,ImageView imageView){

        String key = getKey(url);
        //内存查找
        Bitmap bitmap = getBitmapFromMemory(key);
        if (bitmap != null){
            //这里判断是preload的bitmap还是recycler的bitmap
            if (imageView.getTag() == null){
                Log.d(TAG, "loadBitmap: imageView tag is null!");
                imageView.setImageBitmap(bitmap);
                return;
            }
            //recycler的bitmap
            if (url.equals(imageView.getTag())){
//                Log.d(TAG, "loadBitmap: true  from memory  url:" +url+
//                        "-------------------------------------------------------------------------------");
                imageView.setImageBitmap(bitmap);
            }else{
//                Log.d(TAG, "loadBitmap: false from memory  url:" +url+" imageView url"+imageView.getTag()+
//                        "-------------------------------------------------------------------------------");
//                //imageView.setImageResource(R.drawable.android);
//                Log.d(TAG, "run: the view has change!From the memory!");
            }
            return;
        }
        //从本地或者网络拉取
        mOkHttpEngine.getAsyncHttp(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "onError: "+call.request().toString(),e );
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                //Log.d(TAG, "onResponse: "+url+" network: "+response.networkResponse());
                Log.d(TAG, "onResponse: "+url+" cache: "+response.cacheResponse());

                if (response.body() == null){
                    //Log.d(TAG, "onResponse: the response body is null!");
                    return;
                }
                PictureInfo pictureInfo = mPictureTable.query(url);
                if (pictureInfo == null){

                }
                //Log.d(TAG, "run: "+Thread.currentThread().getName());
                BitmapUtil bitmapUtil = new BitmapUtil(response.body().byteStream());
                int width = bitmapUtil.getWidth();
                int height = bitmapUtil.getHeight();

                int reqWidth = imageView.getWidth();
                int reqHeight = imageView.getHeight();

                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                Bitmap bitmap1;
                if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT){
                    bitmap1 = bitmapUtil.decodeBitmapFromBytes(reqWidth,reqHeight);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap1);
                        }
                    });
                }else{
//                            int scale = width/reqWidth;         //原始宽度处以imageView的宽，得到比例
//                            layoutParams.height = height/scale;           //得到imageView的高度
//                            imageView.setLayoutParams(layoutParams);      //设置View宽高
//                            reqHeight = imageView.getHeight();           //重新得到新的高度
                    bitmap1 = bitmapUtil.decodeBitmapFromBytes(reqWidth,reqHeight);
                    addBitmapToMemoryCache(key,bitmap1);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (url.equals(imageView.getTag())){
//                                Log.d(TAG, "post: true  from network or disk  url:" +url+
//                                        "-------------------------------------------------------------------------------");
                                imageView.setImageBitmap(bitmap1);
                            }else{
//                                Log.d(TAG, "post: false  from network or disk  url:" +url+imageView.getTag()+
//                                        "-------------------------------------------------------------------------------");
                                //imageView.setImageResource(R.drawable.android);
                                //Log.d(TAG, "run: the view has change!From the network or disk!");
                            }
                        }
                    });
                }

//                imageView.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d(TAG, "run: "+Thread.currentThread().getName());
//                        int reqWidth = imageView.getWidth();
//                        int reqHeight = imageView.getHeight();
//
//                        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
//                        Bitmap bitmap1;
//                        if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT){
//                            bitmap1 = bitmapUtil.decodeBitmapFromBytes(reqWidth,reqHeight);
//                            imageView.setImageBitmap(bitmap1);
//                        }else{
////                            int scale = width/reqWidth;         //原始宽度处以imageView的宽，得到比例
////                            layoutParams.height = height/scale;           //得到imageView的高度
////                            imageView.setLayoutParams(layoutParams);      //设置View宽高
////                            reqHeight = imageView.getHeight();           //重新得到新的高度
//                            bitmap1 = bitmapUtil.decodeBitmapFromBytes(reqWidth,reqHeight);
//
//                            if (url.equals(imageView.getTag())){
//                                imageView.setImageBitmap(bitmap1);
//                            }else{
//                                Log.d(TAG, "run: the view has change!From the network or disk!");
//                            }
//                        }
//                    }
//                });
            }
        });
    }

    private Bitmap getBitmapFromMemory(String key){
        return mMemoryCache.get(key);
    }

    private void addBitmapToMemoryCache(String key,Bitmap bitmap){
        if (getBitmapFromMemory(key) == null){
            mMemoryCache.put(key,bitmap);
        }
    }

    //借用了okio
    private String getKey(String url){
        return ByteString.encodeUtf8(url).md5().hex();
    }

}
