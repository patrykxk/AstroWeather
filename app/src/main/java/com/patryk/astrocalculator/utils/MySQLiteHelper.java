package com.patryk.astrocalculator.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Patryk on 2017-06-08.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_CITIES = "cites";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CITY = "city";

    private static final String DATABASE_NAME = "cites.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_CITIES + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CITY
            + " text not null);";


    
    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
