package com.patryk.astrocalculator.fragment;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.astrocalculator.AstroCalculator;
import com.astrocalculator.AstroDateTime;
import com.patryk.astrocalculator.R;
import com.patryk.astrocalculator.SettingsParameters;

import java.util.Calendar;


/**
 * Created by Patryk on 2017-05-07.
 */


public class FragmentMoon extends Fragment {
    private static TextView latitudeTextView;
    private static TextView longitudeTextView;
    private static TextView moonRise;
    private static TextView moonSet;
    private static TextView newMoon;
    private static TextView fullMoon;
    private static TextView moonphase;
    private static TextView lunarDay;

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable runnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_moon, container, false);
        latitudeTextView = (TextView) view.findViewById(R.id.latitudeTextView);
        longitudeTextView = (TextView) view.findViewById(R.id.longitudeTextView);
        moonRise = (TextView) view.findViewById(R.id.moonriseTime);
        moonSet = (TextView) view.findViewById(R.id.moonsetTime);
        newMoon = (TextView) view.findViewById(R.id.newMoonTime);
        fullMoon = (TextView) view.findViewById(R.id.fullMoon);
        moonphase = (TextView) view.findViewById(R.id.moonPhase);
        lunarDay = (TextView) view.findViewById(R.id.synodicDay);

        latitudeTextView.setText(getString(R.string.latitude) + ": " + String.valueOf(SettingsParameters.latitude));
        longitudeTextView.setText(getString(R.string.longitude) + ": " + String.valueOf(SettingsParameters.longitude));

        runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void run() {
                getMoonInfo();
                handler.postDelayed(runnable, SettingsParameters.refreshTimeInMinutes * 60000);
            }
        };
        new Thread(runnable).start();
        return view;
    }


    private static void getMoonInfo() {

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
        setMoonInfoTextViews(astro.getMoonInfo());
    }

    private static void setMoonInfoTextViews(AstroCalculator.MoonInfo moonInfo) {
            moonRise.setText(String.valueOf(moonInfo.getMoonrise()));
            moonSet.setText(String.valueOf(moonInfo.getMoonset()));
            newMoon.setText(String.valueOf(moonInfo.getNextNewMoon()));
            fullMoon.setText(String.valueOf(moonInfo.getNextFullMoon()));
            int x = (int) (moonInfo.getIllumination()*100);
            moonphase.setText(x + "%");
            lunarDay.setText((String.valueOf(moonInfo.getNextNewMoon())));
    }


    public static void updateInfo(double longitude, double latitude) {
        longitudeTextView.setText("Longitude" + ": " + longitude);
        latitudeTextView.setText("Latitude" + " : "  + latitude);
        getMoonInfo();
    }

}