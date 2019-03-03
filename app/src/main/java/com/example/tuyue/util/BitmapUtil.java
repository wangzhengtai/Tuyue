package com.example.tuyue.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

//bitmap压缩类
public class BitmapUtil {

    private static final String TAG = "BitmapUtil";

    private int mWidth;
    private int mHeight;
    private byte[] mBitmapBytes;
    private BitmapFactory.Options mOptions;

    public BitmapUtil(InputStream inputStream) {
        try {
            mBitmapBytes = getByteArrayFromStream(inputStream);
            mOptions = new BitmapFactory.Options();
            setBitmapBounds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getWidth() {
        return mWidth;
    }

    private void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    private void setHeight(int height) {
        mHeight = height;
    }

    public Bitmap decodeBitmapFromBytes(int reqWidth,int reqHeight){

        mOptions.inSampleSize = calculateInSampleSize(reqWidth,reqHeight);
        mOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(mBitmapBytes,0,mBitmapBytes.length,mOptions);
    }

    private void setBitmapBounds(){
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(mBitmapBytes,0,mBitmapBytes.length,mOptions);
        setWidth(mOptions.outWidth);
        setHeight(mOptions.outHeight);
    }

    private byte[] getByteArrayFromStream(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        byte[] bytes = new byte[1024];
        int len;
        while ((len = bufferedInputStream.read(bytes))>0){
            bufferedOutputStream.write(bytes,0,len);
        }
        bufferedOutputStream.flush();

        inputStream.close();
        bufferedInputStream.close();
        bufferedOutputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    private int calculateInSampleSize(int reqWidth,int reqHeight){
        if (reqWidth == 0 || reqHeight == 0){
            return 1;
        }

        final int width = getWidth();
        final int height = getHeight();

        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth){
            final int halfWidth = width/2;
            final int halfHeight = height/2;

            while ((halfWidth/inSampleSize) >= reqWidth && (halfHeight/inSampleSize) >= reqHeight){
                Log.d(TAG, "calculateInSampleSize: halfWidth:"+halfWidth);
                Log.d(TAG, "calculateInSampleSize: halfHeight"+halfHeight);
                Log.d(TAG, "calculateInSampleSize: inSampleSize"+inSampleSize);
                Log.d(TAG, "calculateInSampleSize: reqWidth"+reqWidth);
                Log.d(TAG, "calculateInSampleSize: reqHeight"+reqHeight);

                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
}
