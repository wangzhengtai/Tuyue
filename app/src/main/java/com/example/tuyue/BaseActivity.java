package com.example.tuyue;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {

    private NetworkChangeReceiver mNetworkChangeReceiver;
    private View mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        //intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        //intentFilter.addAction("android.net.wifi.STATE_CHANGE");

        mView = getWindow().getDecorView().getRootView();

        mNetworkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(mNetworkChangeReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mNetworkChangeReceiver);
    }

    protected void setSnackView(View view){
        mView = view;
    }

    private class NetworkChangeReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

//            Network[] networks = connectivityManager.getAllNetworks();
//            boolean net = false;
//            for (Network network : networks) {
//                NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
//                if (networkInfo.isConnected()) {
//                    net = true;
//                    break;
//                }
//            }
//            //网络不可用
//            if (!net){
//
//            }

            if (networkInfo == null || !networkInfo.isConnected()){
                Snackbar.make(mView, R.string.network_cannot_connect,Snackbar.LENGTH_LONG)
                        .setAction(R.string.setting, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //跳转到设置界面
                                Intent intent =  new Intent(Settings.ACTION_SETTINGS);
                                startActivity(intent);
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .show();
            }
        }
    }

}
