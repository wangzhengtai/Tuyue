package com.example.tuyue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tuyue.model.Picture;
import com.example.tuyue.util.ImageLoader;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String TAG = "RecyclerAdapter";

    private List<String> mUrls;
    private Context mContext;

    public RecyclerAdapter(List<String> urls){
        mUrls = urls;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (mContext == null){
            mContext = viewGroup.getContext();
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_recycler,viewGroup,false);
        Log.d(TAG, "onCreateViewHolder: i:"+i);
        Log.d(TAG, "onCreateViewHolder: ********************************************************************");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        String url = mUrls.get(i);
        Log.d(TAG, "onBindViewHolder: i:"+i+"  url:"+url);
        Log.d(TAG, "onCreateViewHolder: ********************************************************************");
        viewHolder.mImageView.setTag(url);
        ImageLoader.getInstance(mContext).loadBitmap(url,viewHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mUrls.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView mCardView;
        ImageView mImageView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView = (CardView) itemView;
            mImageView = itemView.findViewById(R.id.item_image);
//            ViewGroup.LayoutParams params = mImageView.getLayoutParams();
//            params.height =  (int) (200 + Math.random() * 400) ;
//            mImageView.setLayoutParams(params);
        }
    }
}
