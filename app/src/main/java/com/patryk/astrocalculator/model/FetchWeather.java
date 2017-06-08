package com.patryk.astrocalculator.model;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.patryk.astrocalculator.R;

/**
 * Created by Patryk on 2017-06-04.
 */

public class FetchWeather {
    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/%s?q=%s&units=metric&cnt=%d";//TODO &cnt={cnt} dodać liczbe dni

    public static JSONObject getJSON(Context context, String responseType, String city){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, responseType, city,11));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
//    public static JSONObject getJSON(Context context, String responseType, String city){
//
//    }
}
