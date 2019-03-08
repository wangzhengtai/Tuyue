package com.example.tuyue;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.tuyue.util.ImageLoader;

import java.lang.ref.WeakReference;

public class PreloadActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_preload);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView imageView = findViewById(R.id.preload_view);
        //必应每日一图
        ImageLoader.getInstance(getApplicationContext())
                .loadBitmap("https://api.dujin.org/bing/1920.php",imageView);

        DelayHandler delayHandler = new DelayHandler(this);
        delayHandler.sendEmptyMessageDelayed(1,500);         //延时3秒后跳转到主页面
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();           //禁用返回键
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mDelayHandler.removeMessages(1);
//    }

    private static class DelayHandler extends Handler{

        private static final String TAG = "DelayHandler";
        private WeakReference<PreloadActivity> mWeakReference;

        DelayHandler(PreloadActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            PreloadActivity activity = mWeakReference.get();
            if (activity != null){
                Intent intent = new Intent(activity,MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }else {
                Log.i(TAG, "handleMessage: activity is null!");
            }
        }
    }
}
