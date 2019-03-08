package com.example.tuyue.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.tuyue.database.DbScheme.PictureInfoTable;
import com.example.tuyue.model.Picture;

import java.util.ArrayList;
import java.util.List;

public class PictureTable {

    private SQLiteDatabase mDatabase;

    public PictureTable(Context context) {
        mDatabase = new SQLiteHelper(context).getWritableDatabase();
    }

    public long insert(Picture picture){
//        String sql = "insert into "+ PictureInfoTable.NAME+" ("+
//                PictureInfoTable.Columns.CID+
//                PictureInfoTable.Columns.URL+
//                ")"+"values(?,?)";
//        String[] bingArgs = new String[]{
//                picture.getCid(),
//                picture.getUrl()
//        };
        ContentValues values = new ContentValues();
        values.put(PictureInfoTable.Columns.CID,picture.getCid());
        values.put(PictureInfoTable.Columns.URL,picture.getUrl());
        return mDatabase.insert(PictureInfoTable.NAME,null,values);
    }

    public int update(String url,int width,int height,int inSimpleSize){
        ContentValues values = new ContentValues();
        values.put(PictureInfoTable.Columns.WIDTH,width);
        values.put(PictureInfoTable.Columns.HEIGHT,height);
        values.put(PictureInfoTable.Columns.INSIMPLESIZE,inSimpleSize);
        return mDatabase.update(PictureInfoTable.NAME,values,"url = ?",new String[]{url});
    }

    public int delete(int cid){
        return mDatabase.delete(PictureInfoTable.NAME,"cid = ?",new String[]{String.valueOf(cid)});
    }

    public List<String> queryUrls(int cid){
        List<String> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(PictureInfoTable.NAME,new String[]{PictureInfoTable.Columns.URL},
                "cid = ?", new String[]{String.valueOf(cid)}, null,null,null);
//        Cursor cursor = mDatabase.rawQuery("select * from "+PictureInfoTable.NAME+" where cid = ?",
//                new String[]{String.valueOf(cid)});
        if (cursor.moveToFirst()){
            do{
                list.add(cursor.getString(cursor.getColumnIndex(PictureInfoTable.Columns.URL)));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public PictureInfo query(String url){
        Cursor cursor = mDatabase.query(PictureInfoTable.NAME,null,"url = ?",
                new String[]{url}, null,null,null);
//        Cursor cursor = mDatabase.rawQuery("select * from "+PictureInfoTable.NAME+" where cid = ?",
//                new String[]{String.valueOf(cid)});
        if (cursor.moveToFirst()){
            PictureInfo pictureInfo = new PictureInfo();
            pictureInfo.setCid(cursor.getInt(cursor.getColumnIndex(PictureInfoTable.Columns.CID)));
            pictureInfo.setUrl(cursor.getString(cursor.getColumnIndex(PictureInfoTable.Columns.URL)));
            pictureInfo.setWidth(cursor.getInt(cursor.getColumnIndex(PictureInfoTable.Columns.WIDTH)));
            pictureInfo.setHeight(cursor.getInt(cursor.getColumnIndex(PictureInfoTable.Columns.HEIGHT)));
            pictureInfo.setInSimpleSize(cursor.getInt(cursor.getColumnIndex(
                    PictureInfoTable.Columns.INSIMPLESIZE)));
            cursor.close();
            return pictureInfo;
        }
        return null;
    }

}
