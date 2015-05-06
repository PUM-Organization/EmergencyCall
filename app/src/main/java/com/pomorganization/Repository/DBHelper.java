package com.pomorganization.Repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by Daniel on 5/6/2015.
 */
public class DBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "emergency.db";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
