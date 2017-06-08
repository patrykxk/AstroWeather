package com.patryk.astrocalculator.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.patryk.astrocalculator.R;
import com.patryk.astrocalculator.SettingsParameters;
import com.patryk.astrocalculator.fragment.FragmentMoon;
import com.patryk.astrocalculator.fragment.FragmentSun;

import java.util.Set;

/**
 * Created by Patryk on 2017-05-07.
 */

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private EditText cityEditText;
    private EditText frequencyEditText;
    private Button applyButton;
    private String city;
    private int frequency;
    private Spinner spinner;
    private String[] arraySpinner;
    private String selectedUnit = SettingsParameters.units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        cityEditText = (EditText) findViewById(R.id.city);
        frequencyEditText = (EditText) findViewById(R.id.frequency);
        applyButton = (Button) findViewById(R.id.apply);

        this.arraySpinner = new String[]{
                "Metric", "Imperial"
        };
        spinner = (Spinner) findViewById(R.id.unitSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        int spinnerPosition = adapter.getPosition(SettingsParameters.units);
        spinner.setSelection(spinnerPosition);

        setButtonListener();
        this.cityEditText.setText(SettingsParameters.cityName);
        this.frequencyEditText.setText(String.valueOf(SettingsParameters.refreshTimeInMinutes));
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        selectedUnit = String.valueOf(parent.getItemAtPosition(pos));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setButtonListener() {
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(frequencyEditText.getText().toString().trim())) {
                    showMessage("Enter values!");
                    return;
                }
                city = String.valueOf(cityEditText.getText());
                frequency = Integer.parseInt(String.valueOf(frequencyEditText.getText()));

                SettingsParameters.cityName = city;
                SettingsParameters.refreshTimeInMinutes = frequency;
                SettingsParameters.units = selectedUnit;
                FragmentSun.updateInfo(SettingsParameters.longitude, SettingsParameters.latitude);
                FragmentMoon.updateInfo(SettingsParameters.longitude, SettingsParameters.latitude);
                showMessage("Settings applied!");

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
