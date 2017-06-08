package com.patryk.astrocalculator.model;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Patryk on 2017-06-08.
 */

public class FileManager {
    public static void saveJson(Context context, JSONObject jsonObject, String filename){
        String content = jsonObject.toString();

        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject readJson(Context context, String filename) {
        String response = "";
        try {
            FileInputStream is = context.openFileInput(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            response = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject json = null;
        try {
            json = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }
}
