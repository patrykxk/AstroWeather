package com.patryk.astrocalculator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.patryk.astrocalculator.R;
import com.patryk.astrocalculator.data.SettingsParameters;
import com.patryk.astrocalculator.model.City;
import com.patryk.astrocalculator.utils.CitiesDataSource;

import java.util.List;

import static android.widget.AdapterView.OnItemClickListener;
import static android.widget.AdapterView.OnItemLongClickListener;

/**
 * Created by Patryk on 2017-06-08.
 */
public class CitiesActivity extends AppCompatActivity implements OnItemLongClickListener, OnItemClickListener {
    ArrayAdapter<City> adapter;
    ListView citiesListView;
    private Button addButton;
    private CitiesDataSource datasource;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);


        datasource = new CitiesDataSource(this);
        datasource.open();
        List<City> values = datasource.getAllCitys();
        adapter = new ArrayAdapter<City>(this,
                android.R.layout.simple_list_item_1, values);

        citiesListView = (ListView) findViewById(R.id.citiesListView);
        citiesListView.setAdapter(adapter);

        if (values.isEmpty()) {
            adapter.add(datasource.createCity("Lodz"));
        }

        addButton = (Button) findViewById(R.id.addButton);
        setAddButtonListener();
        citiesListView.setOnItemLongClickListener(this);
        citiesListView.setOnItemClickListener(this);
    }


    private void setAddButtonListener() {
        final CitiesActivity that = this;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(that);
                builder.setTitle("Enter city name");

                final EditText input = new EditText(that);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(10,0,10,0);
                input.setLayoutParams(lp);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = input.getText().toString();
                        adapter.add(datasource.createCity(name));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }


    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete city?")
                .setCancelable(true)
                .setPositiveButton("Ok, Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                City city = adapter.getItem(position);
                                datasource.deleteCity(city);
                                adapter.remove(city);
                            }
                        })
                .setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SettingsParameters.cityName = adapter.getItem(position).toString();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
