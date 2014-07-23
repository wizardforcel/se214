package com.example.wizard.myapplication.utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Wizard on 2014/7/23.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final String DB_NAME = "data.db"; //数据库名称
    private static final int version = 1; //数据库版本

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "CREATE TABLE note (" +
                     "id INTEGER PRIMARY KEY," +
                     "date INT," +
                     "starttime INT," +
                     "endtime INT," +
                     "title TEXT," +
                     "comment TEXT," +
                     "alerttype INT," +
                     "alerttime INT )";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }

}