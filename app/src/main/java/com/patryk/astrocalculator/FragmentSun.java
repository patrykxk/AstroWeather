package com.patryk.astrocalculator;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;

import java.util.Calendar;

/**
 * Created by Patryk on 2017-05-07.
 */

public class FragmentSun extends Fragment {
    private static TextView latitudeTextView;
    private static TextView longitudeTextView;
    private static TextView sunriseTime;
    private static TextView sunriseAzimuth;
    private static TextView sunsetTime;
    private static TextView sunsetAzimuth;
    private static TextView duskTime;
    private static TextView dawnTime;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sun, container, false);
        latitudeTextView = (TextView) view.findViewById(R.id.latitudeTextView);
        longitudeTextView = (TextView) view.findViewById(R.id.longitudeTextView);
        sunriseTime = (TextView) view.findViewById(R.id.sunriseTime);
        sunriseAzimuth = (TextView) view.findViewById(R.id.sunriseAzimuth);
        sunsetTime = (TextView) view.findViewById(R.id.sunsetTime);
        sunsetAzimuth = (TextView) view.findViewById(R.id.sunsetAzimuth);
        duskTime = (TextView) view.findViewById(R.id.duskTime);
        dawnTime = (TextView) view.findViewById(R.id.dawnTime);

        latitudeTextView.setText(getString(R.string.latitude) + ": " + String.valueOf(SettingsParameters.latitude));
        longitudeTextView.setText(getString(R.string.longitude) + ": " + String.valueOf(SettingsParameters.longitude));

        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                    getSunInfo();
                handler.postDelayed(runnable, SettingsParameters.refreshTimeInMinutes * 60000);
            }
        };
        new Thread(runnable).start();

        return view;
    }

    private static void getSunInfo() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int timezoneOffset = calendar.get(Calendar.ZONE_OFFSET);
        boolean daylightSaving = true;

        AstroDateTime astroDateTime = new AstroDateTime(year,month,day,hour,minute,second,timezoneOffset,daylightSaving);
        AstroCalculator astro = new AstroCalculator(astroDateTime, new AstroCalculator.Location(SettingsParameters.latitude, SettingsParameters.longitude) );
        setSunInfoTextViews(astro.getSunInfo());
    }

    private static void setSunInfoTextViews(AstroCalculator.SunInfo sunInfo){
        sunriseTime.setText("Time" + ": " + sunInfo.getSunrise().toString());
        sunriseAzimuth.setText("Azimuth" + " : "  + String.valueOf(sunInfo.getAzimuthRise()));
        sunsetTime.setText("Time" + ": " + sunInfo.getSunset().toString());
        sunsetAzimuth.setText("Azimuth" + " : " + String.valueOf(sunInfo.getAzimuthSet()));
        duskTime.setText(sunInfo.getTwilightEvening().toString());
        dawnTime.setText(sunInfo.getTwilightMorning().toString());
    }

    public static void setLocationTextViews(double longitude, double latitude) {
        longitudeTextView.setText("Longitude" + ": " + longitude);
        latitudeTextView.setText("Latitude" + " : "  + latitude);
        getSunInfo();
    }
}