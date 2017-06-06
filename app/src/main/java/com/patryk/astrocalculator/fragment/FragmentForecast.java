package com.patryk.astrocalculator.fragment;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.patryk.astrocalculator.R;
import com.patryk.astrocalculator.SettingsParameters;
import com.patryk.astrocalculator.activity.MainActivity;
import com.patryk.astrocalculator.model.FetchWeather;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Patryk on 2017-06-04.
 */

public class FragmentForecast extends Fragment {

        Typeface weatherFont;
        TextView cityField;
        TextView coordinatesField;
        List<TextView> dateTextFields = new ArrayList<>();
        List<TextView> iconsFields = new ArrayList<>();
        List<TextView> temperaturesFields = new ArrayList<>();


        Handler handler;

        public FragmentForecast(){
            handler = new Handler();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
            cityField = (TextView)rootView.findViewById(R.id.city_field);
            coordinatesField = (TextView)rootView.findViewById(R.id.coordinates_field);
            for (int i = 0; i < 5 ; i++){
                String id = "date"+(i+1);
                int resID = getResources().getIdentifier(id, "id", MainActivity.PACKAGE_NAME);
                dateTextFields.add((TextView)rootView.findViewById(resID));

                id = "weather_icon"+(i+1);
                resID = getResources().getIdentifier(id, "id", MainActivity.PACKAGE_NAME);
                iconsFields.add((TextView)rootView.findViewById(resID));
                iconsFields.get(i).setTypeface(weatherFont);

                id = "temperature"+(i+1);
                resID = getResources().getIdentifier(id, "id", MainActivity.PACKAGE_NAME);
                temperaturesFields.add((TextView)rootView.findViewById(resID));
            }

            updateForecastData(SettingsParameters.cityName);
            return rootView;
        }
    @Override
    public void onResume() {
        super.onResume();
        updateForecastData(SettingsParameters.cityName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
    }

    public void updateForecastData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = FetchWeather.getJSON(getActivity(), "forecast/daily", city);
                if(json == null){
                    handler.post(new Runnable(){
                        public void run(){
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable(){
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        public void run(){
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void renderWeather(JSONObject json){
        try {

            JSONObject city = json.getJSONObject("city");
            cityField.setText(city.getString("name").toUpperCase() +
                    ", " +
                    city.getString("country"));

            JSONArray list = json.getJSONArray("list");
            JSONObject coord = city.getJSONObject("coord");

            Double lat = coord.getDouble("lat");
            Double lon = coord.getDouble("lon");
            SettingsParameters.latitude = lat;
            SettingsParameters.longitude = lon;

            coordinatesField.setText("Latitude: " + String.format(Locale.UK, "%.2f", lat) +
                            "\n" + " Longitude: " + String.format(Locale.UK, "%.2f",lon) );

            for (int i = 0; i < temperaturesFields.size() ; i++) {
                JSONObject temp = list.getJSONObject(i).getJSONObject("temp");
                String tempDay =  String.format(Locale.UK, "%.2f", temp.getDouble("day"));

                JSONObject weather = list.getJSONObject(i).getJSONArray("weather").getJSONObject(0);
                String icon = getWeatherIcon(weather.getInt("id"));
                iconsFields.get(i).setText(icon);

                Date date = new Date(list.getJSONObject(i).getLong("dt")*1000);
                dateTextFields.get(i).setText(new SimpleDateFormat("E dd.MM").format(date));
                temperaturesFields.get(i).setText(tempDay + "℃");
            }


        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private String getWeatherIcon(int actualId){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            icon = getActivity().getString(R.string.weather_sunny);
        } else {
            switch(id) {
                case 2 : icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3 : icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7 : icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8 : icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6 : icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5 : icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        return icon;
    }
    public void changeCity(String city){
        updateForecastData(city);
    }

}
