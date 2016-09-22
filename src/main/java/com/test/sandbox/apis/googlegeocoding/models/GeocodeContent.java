package com.test.sandbox.apis.googlegeocoding.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Crystal on 25/05/2015.
 */
public class GeocodeContent implements Serializable {

    private List<AddressComponents> address_components;
    private String formatted_address;
    private Geometry geometry;
    private String place_id;
    private List<String> types;
    private List<String> postcode_localities;
    private boolean partial_match;

    public boolean isPartial_match() {
        return partial_match;
    }

    public void setPartial_match(boolean partial_match) {
        this.partial_match = partial_match;
    }

    public List<AddressComponents> getAddress_components() {
        return address_components;
    }

    public void setAddress_components(List<AddressComponents> address_components) {
        this.address_components = address_components;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public List<String> getPostcode_localities() {
        return postcode_localities;
    }

    public void setPostcode_localities(List<String> postcode_localities) {
        this.postcode_localities = postcode_localities;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
