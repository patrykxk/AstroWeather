package com.patryk.astrocalculator;

import android.os.Bundle;
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


public class MoonFragment extends Fragment {
    private static TextView latitudeTextView;
    private static TextView longitudeTextView;
    private static TextView moonRise;
    private static TextView moonSet;
    private static TextView newMoon;
    private static TextView fullMoon;
    private static TextView moonphase;
    private static TextView lunarDay;

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

        getMoonInfo();

        longitudeTextView.setText("Longitude : " + String.valueOf(SettingsParameters.longitude));
        latitudeTextView.setText("Latitude : " + String.valueOf(SettingsParameters.latitude));

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
            moonphase.setText(String.valueOf(moonInfo.getIllumination()));
            lunarDay.setText((String.valueOf(moonInfo.getNextNewMoon())));
    }


    public static void setLocation(double longitude, double latitude) {
        longitudeTextView.setText("Longitude : " + longitude);
        latitudeTextView.setText("Latitude : " + latitude);
        getMoonInfo();
    }

}