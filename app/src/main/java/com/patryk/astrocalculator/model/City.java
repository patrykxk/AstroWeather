package com.patryk.astrocalculator.model;

/**
 * Created by Patryk on 2017-06-08.
 */

public class City {
    private long id;
    private String cityName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return cityName;
    }
}
