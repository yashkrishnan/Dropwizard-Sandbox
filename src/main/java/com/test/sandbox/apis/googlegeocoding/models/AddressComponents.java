package com.test.sandbox.apis.googlegeocoding.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Crystal on 25/05/2015.
 */
public class AddressComponents implements Serializable {
    private List<String> types;
    private List<String> postcode_localities;
    private String short_name;
    private String long_name;

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getPostcode_localities() {
        return postcode_localities;
    }

    public void setPostcode_localities(List<String> postcode_localities) {
        this.postcode_localities = postcode_localities;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getLong_name() {
        return long_name;
    }

    public void setLong_name(String long_name) {
        this.long_name = long_name;
    }
}
