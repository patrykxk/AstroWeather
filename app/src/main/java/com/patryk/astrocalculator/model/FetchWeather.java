package com.patryk.astrocalculator.model;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.patryk.astrocalculator.R;
import com.patryk.astrocalculator.data.SettingsParameters;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by Patryk on 2017-06-04.
 */

public class FetchWeather {
    private static final String OPEN_WEATHER_MAP_API =
            "http://api.openweathermap.org/data/2.5/%s?q=%s&units=%s&cnt=%d";

    public static JSONObject getJSON(Context context, Handler handler, String responseType, String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API, responseType, city, SettingsParameters.units, 11));
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while ((tmp = reader.readLine()) != null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());
            String filename;
            if (responseType.equals("forecast/daily")) {
                filename = "forecast";
            } else {
                filename = responseType;
            }
            FileManager.saveJson(context, data, filename);
            // This value will be 404 if the request was not
            // successful
            if (data.getInt("cod") != 200) {
                return null;
            }

            return data;
        } catch (UnknownHostException e) {
            e.printStackTrace();
            final Context context1 = context;
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(context1, context1.getString(R.string.no_internet_connection),
                            Toast.LENGTH_LONG).show();
                }
            });

            String filename;
            if (responseType.equals("forecast/daily")) {
                filename = "forecast";
            } else {
                filename = responseType;
            }
            JSONObject json = FileManager.readJson(context, filename);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//    public static JSONObject getJSON(Context context, String responseType, String city){
//
//    }
}
