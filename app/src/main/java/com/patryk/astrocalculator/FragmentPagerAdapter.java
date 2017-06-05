package com.patryk.astrocalculator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;

/**
 * Created by Patryk on 2017-05-07.
 */

public class FragmentPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_ITEMS = 3;
    private String titles[] = {"Weather", "Sun", "Moon"};

    public FragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentWeather();
            case 1:
                return new FragmentSun();
            case 2:
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
