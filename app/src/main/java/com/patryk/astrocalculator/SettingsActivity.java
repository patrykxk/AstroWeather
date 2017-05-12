package com.patryk.astrocalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Patryk on 2017-05-07.
 */

public class SettingsActivity extends AppCompatActivity{

        private EditText latitudeEditText;
        private EditText longitudeEditText;
        private EditText frequencyEditText;
        private Button applyButton;
        private double latitude;
        private double longitude;
        private int frequency;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_settings);
            latitudeEditText  =(EditText)findViewById(R.id.latitude);
            longitudeEditText =(EditText)findViewById(R.id.longtitude);
            frequencyEditText  =(EditText)findViewById(R.id.frequency);
            applyButton = (Button) findViewById(R.id.apply);

            setButtonListener();
            this.latitudeEditText.setText(String.valueOf(SettingsParameters.longitude));
            this.longitudeEditText.setText(String.valueOf(SettingsParameters.latitude));
            this.frequencyEditText.setText(String.valueOf(SettingsParameters.refreshTimeInMinutes));
        }

    private void setButtonListener() {
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(latitudeEditText.getText().toString().trim()) || "".equals(longitudeEditText.getText().toString().trim())
                        || "".equals(frequencyEditText.getText().toString().trim())) {
                    showMessage("Enter values!");
                    return;
                }
                latitude = Double.parseDouble(String.valueOf(latitudeEditText.getText()));
                longitude = Double.parseDouble(String.valueOf(longitudeEditText.getText()));
                frequency = Integer.parseInt(String.valueOf(frequencyEditText.getText()));

                SettingsParameters.longitude = longitude;
                SettingsParameters.latitude = latitude;
                SettingsParameters.refreshTimeInMinutes = frequency;
                if(longitude>180 || longitude<-180 || latitude<-90 || latitude>90){
                    showMessage("Enter correct values!");
                }else {
                    FragmentSun.updateInfo(SettingsParameters.longitude, SettingsParameters.latitude);
                    FragmentMoon.updateInfo(SettingsParameters.longitude, SettingsParameters.latitude);
                    showMessage("Settings applied!");
                }
            }
        });
    }
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
