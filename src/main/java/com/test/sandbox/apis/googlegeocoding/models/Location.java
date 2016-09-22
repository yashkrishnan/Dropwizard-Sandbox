package com.test.sandbox.apis.googlegeocoding.models;

import java.io.Serializable;

/**
 * Created by Crystal on 25/05/2015.
 */
public class Location implements Serializable {
    private double lat;
    private double lng;

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
