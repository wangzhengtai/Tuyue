package com.example.tuyue;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
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

        initViewPager();
        //getTitleFromNetwork();
    }

    private void initViewPager(){
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        List<String> titles = new ArrayList<>();
//        List<Category> categoryList = datas.getData();
//        for (Category category:categoryList){
//            titles.add(category.getName());
//        }

        //图片种类id  pair.first  图片种类名称 pair.second
        List<Pair<Integer,String>> pairs = initPairs();
        for (Pair<Integer,String> pair:pairs){
            titles.add(pair.second);
        }
        for (int i=0;i<titles.size();i++){
            mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(i)));
        }

        List<Fragment> fragments = new ArrayList<>();
        for (int i=0;i<titles.size();i++){
            int cid = pairs.get(i).first;
            fragments.add(RecyclerFragment.newInstance(cid));
        }

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(),titles,fragments);
        mViewPager.setAdapter(pagerAdapter);
        //将TabLayout与ViewPager关联
        mTabLayout.setupWithViewPager(mViewPager);
        //mTabLayout.setTabsFromPagerAdapter(pagerAdapter);   已废弃
    }

    private List<Pair<Integer,String>> initPairs(){
        //图片种类id  pair.first  图片种类名称 pair.second
        List<Pair<Integer,String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>(36,"4K专区"));
        pairs.add(new Pair<>(6,"美女模特"));
        pairs.add(new Pair<>(30,"爱情图片"));
        pairs.add(new Pair<>(9,"风景大片"));
        pairs.add(new Pair<>(15,"小清新"));
        pairs.add(new Pair<>(26,"动漫卡通"));
        pairs.add(new Pair<>(11,"明星风尚"));
        pairs.add(new Pair<>(14,"萌宠动物"));
        pairs.add(new Pair<>(5,"游戏壁纸"));
        pairs.add(new Pair<>(12,"汽车天下"));
        pairs.add(new Pair<>(10,"炫酷时尚"));
        pairs.add(new Pair<>(22,"军事天地"));
        pairs.add(new Pair<>(16,"劲爆体育"));
        pairs.add(new Pair<>(32,"纹理"));
        pairs.add(new Pair<>(35,"文字控"));
        pairs.add(new Pair<>(1,"限时壁纸"));
        return pairs;
    }

//    private void getTitleFromNetwork(){
//        String url = "http://wallpaper.apc.360.cn/index.php?c=WallPaperAndroid&a=getAllCategories";
//        //传入applicationContext防止内存泄漏
//        OkHttpEngine okHttpEngine = OkHttpEngine.getInstance(getApplicationContext());
//        okHttpEngine.getAsyncHttp(url, new Callback() {
//            @Override
//            public void onFailure(@NonNull Call call, @NonNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                Gson gson = new Gson();
//                if (response.body() != null) {
//                    Datas<Category> datas = gson.fromJson(response.body().string(),
//                            new TypeToken<Datas<Category>>(){}.getType());
//
//                    //主线程
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            initViewPager(datas);
//                        }
//                    });
//                }
//            }
//        });
//    }

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
