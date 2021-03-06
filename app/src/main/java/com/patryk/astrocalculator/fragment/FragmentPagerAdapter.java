package com.patryk.astrocalculator.fragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Patryk on 2017-05-07.
 */

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 4;
    private String titles[] = {"Weather","Forecast", "Sun", "Moon"};

    public FragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentWeather();
            case 1:
                return new FragmentForecast();
            case 2:
                return new FragmentSun();
            case 3:
                return new FragmentMoon();

        }
        return null;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
