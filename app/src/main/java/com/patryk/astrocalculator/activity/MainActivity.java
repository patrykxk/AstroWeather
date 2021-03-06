package com.patryk.astrocalculator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.patryk.astrocalculator.R;
import com.patryk.astrocalculator.ZoomOutPageTransformer;
import com.patryk.astrocalculator.fragment.FragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {
    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!getResources().getBoolean(R.bool.isTablet)) {
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
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.About:
                Intent intent2 = new Intent(this, AboutActivity.class);
                startActivity(intent2);
                return true;
            case R.id.Cities:
                Intent intent3 = new Intent(this, CitiesActivity.class);
                startActivity(intent3);
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
}
