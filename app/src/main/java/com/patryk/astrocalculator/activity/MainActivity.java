package com.patryk.astrocalculator.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.patryk.astrocalculator.R;
import com.patryk.astrocalculator.ZoomOutPageTransformer;
import com.patryk.astrocalculator.fragment.FragmentPagerAdapter;
import com.patryk.astrocalculator.fragment.FragmentWeather;
import com.patryk.astrocalculator.model.CityPreference;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(!getResources().getBoolean(R.bool.isTablet)) {
            ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
            viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

            adapterViewPager = new FragmentPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapterViewPager);
        }

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.Settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.About:
                Intent intent2 = new Intent(this, AboutActivity.class);
                startActivity(intent2);
                return true;
            case R.id.change_city:
                showInputDialog();
                return true;
            case R.id.Exit:
                Intent intentExit = new Intent(getApplicationContext(), MainActivity.class);
                intentExit.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentExit.putExtra("EXIT", true);
                startActivity(intentExit);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    private void showInputDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city){
        FragmentWeather wf = (FragmentWeather)getSupportFragmentManager()
                .findFragmentById(R.id.viewPager);

        wf.changeCity("Warszawa");
        new CityPreference(this).setCity("Warszawa");
    }



}
