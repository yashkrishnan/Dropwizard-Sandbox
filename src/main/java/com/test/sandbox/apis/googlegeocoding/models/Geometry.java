package com.test.sandbox.apis.googlegeocoding.models;

import java.io.Serializable;

/**
 * Created by Crystal on 25/05/2015.
 */
public class Geometry implements Serializable {
    private ViewPort viewport;
    private Bounds bounds;
    private String location_type;
    private Location location;

    public ViewPort getViewport() {
        return viewport;
    }

    public void setViewport(ViewPort viewport) {
        this.viewport = viewport;
    }

    public Bounds getBounds() {
        return bounds;
    }

    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
