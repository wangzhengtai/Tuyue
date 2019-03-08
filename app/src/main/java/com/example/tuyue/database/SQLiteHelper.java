package com.example.tuyue.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.tuyue.database.DbScheme.PictureInfoTable;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Tuyue.db";

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ PictureInfoTable.NAME+"("+
                "_id integer primary key autoincrement,"+
                PictureInfoTable.Columns.CID +","+
                PictureInfoTable.Columns.URL+","+
                PictureInfoTable.Columns.WIDTH+","+
                PictureInfoTable.Columns.HEIGHT+","+
                PictureInfoTable.Columns.INSIMPLESIZE+
                ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
