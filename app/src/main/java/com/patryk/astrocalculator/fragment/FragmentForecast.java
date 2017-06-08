package com.patryk.astrocalculator.fragment;

import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.patryk.astrocalculator.R;
import com.patryk.astrocalculator.data.SettingsParameters;
import com.patryk.astrocalculator.model.FetchWeather;

import org.json.JSONArray;
import org.json.JSONObject;

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
    SwipeRefreshLayout mySwipeRefreshLayout;
    final int NUMBER_OF_FORECASTS = 10;
    String currentCity = "";
    Runnable runnable = null;
    Handler handler;
    private Handler looperHandler = new Handler(Looper.getMainLooper());

    public FragmentForecast() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        cityField = (TextView) rootView.findViewById(R.id.city_field);
        coordinatesField = (TextView) rootView.findViewById(R.id.coordinates_field);
        mySwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshForecast);

        for (int i = 1; i <= NUMBER_OF_FORECASTS; i++) {
            String id = "date" + (i);
            int resID = getResources().getIdentifier(id, "id", getContext().getPackageName());
            dateTextFields.add((TextView) rootView.findViewById(resID));

            id = "weather_icon" + (i);
            resID = getResources().getIdentifier(id, "id", getContext().getPackageName());
            iconsFields.add((TextView) rootView.findViewById(resID));
            iconsFields.get(i - 1).setTypeface(weatherFont);

            id = "temperature" + (i);
            resID = getResources().getIdentifier(id, "id", getContext().getPackageName());
            temperaturesFields.add((TextView) rootView.findViewById(resID));
        }

        updateForecastData(SettingsParameters.cityName);


        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                updateForecastData(SettingsParameters.cityName);
                looperHandler.postDelayed(runnable, SettingsParameters.refreshTimeInMinutes * 60000);
            }
        };
        new Thread(runnable).start();
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!currentCity.equals(SettingsParameters.cityName)) {
            updateForecastData(SettingsParameters.cityName);
        }
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        updateForecastData(SettingsParameters.cityName);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        updateForecastData(SettingsParameters.cityName);
                    }
                }
        );
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");
    }

    public void updateForecastData(final String city) {
        currentCity = city;
        new Thread() {
            public void run() {
                final JSONObject json = FetchWeather.getJSON(getActivity(), handler, "forecast/daily", city);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
        mySwipeRefreshLayout.setRefreshing(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void renderWeather(JSONObject json) {
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
                    "\n" + " Longitude: " + String.format(Locale.UK, "%.2f", lon));
            for (int i = 0; i < NUMBER_OF_FORECASTS; i++) {
                Date date = new Date(list.getJSONObject(i + 1).getLong("dt") * 1000);
                dateTextFields.get(i).setText(new SimpleDateFormat("E dd.MM", Locale.ENGLISH).format(date));

                JSONObject weather = list.getJSONObject(i + 1).getJSONArray("weather").getJSONObject(0);
                String icon = getWeatherIcon(weather.getInt("id"));
                iconsFields.get(i).setText(icon);

                JSONObject temp = list.getJSONObject(i + 1).getJSONObject("temp");

                StringBuilder temperature = new StringBuilder(String.format(Locale.UK, "%d", (int) temp.getDouble("day")));
                if (SettingsParameters.units.equalsIgnoreCase("Metric")) {
                    temperature.append("ºC");
                } else if (SettingsParameters.units.equalsIgnoreCase("Imperial")) {
                    temperature.append("ºF");
                }
                temperaturesFields.get(i).setText(temperature);
            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private String getWeatherIcon(int actualId) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            icon = getActivity().getString(R.string.weather_sunny);
        } else {
            switch (id) {
                case 2:
                    icon = getActivity().getString(R.string.weather_thunder);
                    break;
                case 3:
                    icon = getActivity().getString(R.string.weather_drizzle);
                    break;
                case 7:
                    icon = getActivity().getString(R.string.weather_foggy);
                    break;
                case 8:
                    icon = getActivity().getString(R.string.weather_cloudy);
                    break;
                case 6:
                    icon = getActivity().getString(R.string.weather_snowy);
                    break;
                case 5:
                    icon = getActivity().getString(R.string.weather_rainy);
                    break;
            }
        }
        return icon;
    }

    public void changeCity(String city) {
        updateForecastData(city);
    }

}
