package com.example.tuyue;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tuyue.model.Datas;
import com.example.tuyue.model.Category;
import com.example.tuyue.util.OkHttpEngine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class MainActivity extends BaseActivity {

    private DrawerLayout mDrawerLayout;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }

        FloatingActionButton floatingActionButton = findViewById(R.id.float_button);

        setSnackView(floatingActionButton);          //设置Snackbar的view

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点击了float_button");
                //getTitleFromNetwork();
            }
        });

        //initViewPager();
        getTitleFromNetwork();
    }

    private void initViewPager(Datas datas){
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        List<String> titles = new ArrayList<>();
        List<Category> categoryList = datas.getData();
        for (Category category:categoryList){
            titles.add(category.getName());
        }

        for (int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        List<Fragment> fragments = new ArrayList<>();
        for (int i=0;i<titles.size();i++){
            int cid = Integer.parseInt(categoryList.get(i).getId());
            fragments.add(RecyclerFragment.newInstance(cid));
        }

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),titles,fragments);
        mViewPager.setAdapter(pagerAdapter);
        //将TabLayout与ViewPager关联
        mTabLayout.setupWithViewPager(mViewPager);
        //mTabLayout.setTabsFromPagerAdapter(pagerAdapter);   已废弃
    }

    private void getTitleFromNetwork(){
        String url = "http://wallpaper.apc.360.cn/index.php?c=WallPaperAndroid&a=getAllCategories";
        //传入applicationContext防止内存泄漏
        OkHttpEngine okHttpEngine = OkHttpEngine.getInstance(getApplicationContext());
        okHttpEngine.getAsyncHttp(url, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                if (response.body() != null) {
                    Datas<Category> datas = gson.fromJson(response.body().string(),
                            new TypeToken<Datas<Category>>(){}.getType());

                    //主线程
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            initViewPager(datas);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
            default:
        }
        return true;
    }

    private void showToast(String str){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();
    }
}
