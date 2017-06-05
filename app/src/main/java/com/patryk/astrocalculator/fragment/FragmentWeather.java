package com.patryk.astrocalculator.fragment;

import android.graphics.Typeface;
import android.icu.text.DateFormat;
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
import com.patryk.astrocalculator.model.FetchWeather;

import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;

/**
 * Created by Patryk on 2017-06-04.
 */

public class FragmentWeather  extends Fragment {

        Typeface weatherFont;
        TextView cityField;
        TextView updatedField;
        TextView detailsField;
        TextView currentTemperatureField;
        TextView weatherIcon;
        TextView coordinatesField;

        Handler handler;

        public FragmentWeather(){
            handler = new Handler();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
            cityField = (TextView)rootView.findViewById(R.id.city_field);
            coordinatesField = (TextView)rootView.findViewById(R.id.coordinates_field);
            updatedField = (TextView)rootView.findViewById(R.id.updated_field);
            detailsField = (TextView)rootView.findViewById(R.id.details_field);
            currentTemperatureField = (TextView)rootView.findViewById(R.id.current_temperature_field);
            weatherIcon = (TextView)rootView.findViewById(R.id.weather_icon);

            weatherIcon.setTypeface(weatherFont);
            updateWeatherData(SettingsParameters.cityName);

            return rootView;
        }
    @Override
    public void onResume() {
        super.onResume();
        updateWeatherData(SettingsParameters.cityName);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
    }

    public void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json = FetchWeather.getJSON(getActivity(), "weather", city);
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
            cityField.setText(json.getString("name").toUpperCase() +
                    ", " +
                    json.getJSONObject("sys").getString("country"));

            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            JSONObject main = json.getJSONObject("main");
            JSONObject coord = json.getJSONObject("coord");
            JSONObject wind = json.getJSONObject("wind");


            Double lat = coord.getDouble("lat");
            Double lon = coord.getDouble("lon");
            SettingsParameters.latitude = lat;
            SettingsParameters.longitude = lon;

            coordinatesField.setText("Latitude: " + String.format(Locale.UK, "%.2f", lat) +
                            "\n" + " Longitude: " + String.format(Locale.UK, "%.2f",lon) );
            detailsField.setText(
                    details.getString("description").toUpperCase() +
                            "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa" +
                            "\n" + "Wind: "+
                            "\n" + "Speed: " + String.format(Locale.UK, "%.2f", wind.getDouble("speed")) + " m/s" +
                            "\n" + "Degree: " + String.format(Locale.UK, "%.2f", wind.getDouble("deg")) + "\u00B0" +
                            "\n" + "Visibility: " + json.getDouble("visibility")/1000 + "km"
                    );

            currentTemperatureField.setText(
                    String.format(Locale.UK, "%.2f", main.getDouble("temp"))+ " ℃");


            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        }catch(Exception e){
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
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
        weatherIcon.setText(icon);
    }
    public void changeCity(String city){
        updateWeatherData(city);
    }

}
