package com.patryk.astrocalculator.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.patryk.astrocalculator.R;

import static com.patryk.astrocalculator.SettingsParameters.citiesList;

/**
 * Created by Patryk on 2017-06-08.
 */
//citiesListView
public class CitiesActivity extends AppCompatActivity  implements AdapterView.OnItemLongClickListener {
    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter=0;
    ListView citiesListView;
    private Button addButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);
        if(citiesList.isEmpty()){
            citiesList.add("Lodz");
        }
        adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                citiesList);
        citiesListView = (ListView)findViewById(R.id.citiesListView);
        citiesListView.setAdapter(adapter);
        addButton = (Button) findViewById(R.id.addButton);
        setAddButtonListener();
        citiesListView.setOnItemLongClickListener(this);

    }


    private void setAddButtonListener() {
        final CitiesActivity that = this;
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(that);
                builder.setTitle("Title");

                final EditText input = new EditText(that);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        citiesList.add(input.getText().toString());
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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int intId = (int) id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete city?")
                .setCancelable(true)
                .setPositiveButton("Ok, Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                citiesList.remove(intId);
                                adapter=new ArrayAdapter<>(CitiesActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        citiesList);
                                citiesListView.setAdapter(adapter);
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


        Log.e("SADASD", "bla bla");
        //showDialog(position);
        return false;
    }
    @Override
    protected Dialog onCreateDialog(final int id) {

        return super.onCreateDialog(id);
    }
}
