package com.example.zjy.mydatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by zjy on 2016/9/7.
 */
public class Mydatabase extends SQLiteOpenHelper {
    public Mydatabase(Context context) {
        super(context, "mydatabase.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table word(_id integer primary key,newword,chinese)");
        Log.d("tag", "onCreate: ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
