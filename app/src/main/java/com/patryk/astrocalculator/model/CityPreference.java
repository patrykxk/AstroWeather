package com.patryk.astrocalculator.model;

import android.content.SharedPreferences;
import android.app.Activity;

/**
 * Created by Patryk on 2017-06-04.
 */

public class CityPreference {
        SharedPreferences prefs;

        public CityPreference(Activity activity){
            prefs = activity.getPreferences(Activity.MODE_PRIVATE);
        }


        public String getCity(){
            return prefs.getString("city", "Lodz, Pl");
        }

        public void setCity(String city){
            prefs.edit().putString("city", city).commit();
        }

}
