package com.example.tuyue;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tuyue.model.Datas;
import com.example.tuyue.model.Picture;
import com.example.tuyue.util.OkHttpEngine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class RecyclerFragment extends Fragment {

    private static final String TAG = "RecyclerFragment";

    //图片基本url
    private static final String url = "http://wallpaper.apc.360.cn/index.php?c=WallPaperAndroid&a=getAppsByCategory";
    private int start = 0;     //跳过的记录数，初始时为0
    private static final int count = 30;           //每次返回30张图片

    private static final String CATEGORY_CID = "CATEGORY_CID";
    private int mCid;          //图片种类id

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;

    public static RecyclerFragment newInstance(int cid) {
        Bundle args = new Bundle();
        args.putInt(CATEGORY_CID,cid);
        RecyclerFragment fragment = new RecyclerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mCid = getArguments().getInt(CATEGORY_CID);
        }
        return inflater.inflate(R.layout.fragment_recycler,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: onScrolled!");
                OkHttpEngine okHttpEngine = OkHttpEngine.getInstance(getContext());
                okHttpEngine.cancelAllCall();
            }
        });
        getPictureFromNetwork();
    }

    private void getPictureFromNetwork(){
        OkHttpEngine okHttpEngine = OkHttpEngine.getInstance(getContext());
        List<Pair<String,String>> pairs = new ArrayList<>();
        pairs.add(new Pair<>("cid",String.valueOf(mCid)));
        pairs.add(new Pair<>("start",String.valueOf(start)));
        pairs.add(new Pair<>("count",String.valueOf(count)));
        okHttpEngine.postAsyncHttp(url, pairs, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Gson gson = new Gson();
                if (response.body() != null) {
                    Datas<Picture> datas = gson.fromJson(response.body().string(),
                            new TypeToken<Datas<Picture>>(){}.getType());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerAdapter = new RecyclerAdapter(datas.getData());
                            mRecyclerView.setAdapter(mRecyclerAdapter);
                        }
                    });
                }
            }
        });
    }
}
