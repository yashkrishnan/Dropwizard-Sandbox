package com.example.sandbox.apis.googlegeocoding.models;

import java.io.Serializable;

/**
 * Created by Crystal on 25/05/2015.
 */
public class ViewPort implements Serializable {

    private Location southwest;
    private Location northeast;

    public Location getSouthwest() {
        return southwest;
    }

    public void setSouthwest(Location southwest) {
        this.southwest = southwest;
    }

    public Location getNortheast() {
        return northeast;
    }

    public void setNortheast(Location northeast) {
        this.northeast = northeast;
    }
}
