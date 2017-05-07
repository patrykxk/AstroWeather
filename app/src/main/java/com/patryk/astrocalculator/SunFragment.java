package com.patryk.astrocalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Patryk on 2017-05-07.
 */


public class SunFragment extends Fragment {
    private static TextView latitudeTextView;
    private static TextView longitudeTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sun, container, false);
        latitudeTextView = (TextView) view.findViewById(R.id.latitudeTextView);
        longitudeTextView = (TextView) view.findViewById(R.id.longitudeTextView);

        longitudeTextView.setText( String.valueOf(SettingsParameters.longtitude));
        latitudeTextView.setText(String.valueOf(SettingsParameters.latitude));

        return view;
    }

    public static void setLocation(double longitude, double latitude) {
        longitudeTextView.setText("Longtitude : " + longitude);
        latitudeTextView.setText("Latitude : " + latitude);
    }
}