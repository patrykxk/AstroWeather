package com.patryk.astrocalculator.fragment;

import android.graphics.Typeface;
import android.icu.text.DateFormat;
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

import org.json.JSONObject;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.RunnableFuture;

/**
 * Created by Patryk on 2017-06-04.
 */

public class FragmentWeather extends Fragment {

    Typeface weatherFont;
    TextView cityField;
    TextView updatedField;
    TextView detailsField;
    TextView currentTemperatureField;
    TextView weatherIcon;
    TextView coordinatesField;
    TextView descriptionField;
    SwipeRefreshLayout mySwipeRefreshLayout;
    String currentCity = "";
    Runnable runnable = null;
    Handler handler;
    private Handler looperHandler = new Handler(Looper.getMainLooper());

    public FragmentWeather() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        cityField = (TextView) rootView.findViewById(R.id.city_field);
        coordinatesField = (TextView) rootView.findViewById(R.id.coordinates_field);
        updatedField = (TextView) rootView.findViewById(R.id.updated_field);
        descriptionField = (TextView) rootView.findViewById(R.id.description);
        detailsField = (TextView) rootView.findViewById(R.id.details_field);
        currentTemperatureField = (TextView) rootView.findViewById(R.id.current_temperature_field);
        weatherIcon = (TextView) rootView.findViewById(R.id.weather_icon);
        mySwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);

        weatherIcon.setTypeface(weatherFont);
        updateWeatherData(SettingsParameters.cityName);
        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                updateWeatherData(SettingsParameters.cityName);
                looperHandler.postDelayed(runnable, SettingsParameters.refreshTimeInMinutes * 60000);
            }
        };
        new Thread(runnable).start();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!currentCity.equals(SettingsParameters.cityName)){
            updateWeatherData(SettingsParameters.cityName);
        }
    }

    @Override
    public void onViewCreated(View rootView, Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        updateWeatherData(SettingsParameters.cityName);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        updateWeatherData(SettingsParameters.cityName);
                    }
                }
        );
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weather.ttf");

    }

    public void updateWeatherData(final String city) {
        currentCity = city;
        new Thread() {
            public void run() {
                final JSONObject json = FetchWeather.getJSON(getActivity(), handler, "weather", city);
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
                    "\n" + " Longitude: " + String.format(Locale.UK, "%.2f", lon));

            descriptionField.setText(details.getString("description").toUpperCase());
            StringBuilder detailsStringBuilder = new StringBuilder(
                    "\n" + "Humidity: " + main.getString("humidity") + "%" +
                            "\n" + "Pressure: " + main.getString("pressure") + " hPa");

            detailsStringBuilder.append("\n" + "Wind: " + "\n" + "Speed: ")
                    .append(String.format(Locale.UK, "%.2f", wind.getDouble("speed")));
            if (SettingsParameters.units.equalsIgnoreCase("Metric")) {
                detailsStringBuilder.append(" m/s");
            } else if (SettingsParameters.units.equalsIgnoreCase("Imperial")) {
                detailsStringBuilder.append(" mil/h");
            }

            if (wind.has("deg")) {
                detailsStringBuilder.append("\n" + "Degree: ").append(String.format(Locale.UK, "%.2f", wind.getDouble("deg"))).append("\u00B0");
            }
            if (json.has("visibility")) {
                detailsStringBuilder.append("\n" + "Visibility: ").append(json.getDouble("visibility") / 1000).append("km");
            }
            detailsField.setText(detailsStringBuilder);

            StringBuilder temperature = new StringBuilder(String.format(Locale.UK, "%d", (int) main.getDouble("temp")));
            if (SettingsParameters.units.equalsIgnoreCase("Metric")) {
                temperature.append("ºC");
            } else if (SettingsParameters.units.equalsIgnoreCase("Imperial")) {
                temperature.append("ºF");
            }
            currentTemperatureField.setText(temperature);

            //DateFormat df = DateFormat.getDateTimeInstance();
            //String updatedOn = df.format(new Date(json.getLong("dt") * 1000));
            //updatedField.setText("Last update: " + updatedOn);

            setWeatherIcon(details.getInt("id"),
                    json.getJSONObject("sys").getLong("sunrise") * 1000,
                    json.getJSONObject("sys").getLong("sunset") * 1000);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

    private void setWeatherIcon(int actualId, long sunrise, long sunset) {
        int id = actualId / 100;
        String icon = "";
        if (actualId == 800) {
            long currentTime = new Date().getTime();
            if (currentTime >= sunrise && currentTime < sunset) {
                icon = getActivity().getString(R.string.weather_sunny);
            } else {
                icon = getActivity().getString(R.string.weather_clear_night);
            }
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
        weatherIcon.setText(icon);
    }

}
