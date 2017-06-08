package com.patryk.astrocalculator.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.patryk.astrocalculator.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Patryk on 2017-06-08.
 */


public class CitiesDataSource {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_CITY };

    public CitiesDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public City createCity(String city) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_CITY, city);
        long insertId = database.insert(MySQLiteHelper.TABLE_CITIES, null,
                values);
        Cursor cursor = database.query(MySQLiteHelper.TABLE_CITIES,
                allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        City newCity = cursorToCity(cursor);
        cursor.close();
        return newCity;
    }

    public void deleteCity(City city) {
        long id = city.getId();
        System.out.println("City deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_CITIES, MySQLiteHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<City> getAllCitys() {
        List<City> cities = new ArrayList<>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_CITIES,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            City city = cursorToCity(cursor);
            cities.add(city);
            cursor.moveToNext();
        }
        cursor.close();
        return cities;
    }

    private City cursorToCity(Cursor cursor) {
        City city = new City();
        city.setId(cursor.getLong(0));
        city.setCityName(cursor.getString(1));
        return city;
    }
}