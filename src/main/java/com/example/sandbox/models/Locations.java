package com.example.sandbox.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.sandbox.models.genericmodels.responsemodels.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Model POJO class for actions on multiple locations
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Locations extends BaseResponse implements Serializable {
    private List<String> locationIds;
    private List<Location> locations;

    public List<String> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(List<String> locationIds) {
        this.locationIds = locationIds;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }
}
