package com.test.sandbox.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.sandbox.models.genericmodels.responsemodels.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Model POJO class for location
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location extends BaseResponse implements Serializable {
    private String locationId;
    private String locationName;

    private String continent;
    private String country;
    private String countryISOCode;
    private String subdivision;
    private String subdivisionISOCode;
    private String city;
    private String streetAddress;
    private String postalCode;

    private Double latitude;
    private Double longitude;
    private String timezone;

    private Double accuracyRadius;
    private String accuracyUnit;

    private List<Double> coordinates;
    private String locationStatus;

    private Long adsCount;

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getContinent() {
        return continent;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryISOCode() {
        return countryISOCode;
    }

    public void setCountryISOCode(String countryISOCode) {
        this.countryISOCode = countryISOCode;
    }

    public String getSubdivision() {
        return subdivision;
    }

    public void setSubdivision(String subdivision) {
        this.subdivision = subdivision;
    }

    public String getSubdivisionISOCode() {
        return subdivisionISOCode;
    }

    public void setSubdivisionISOCode(String subdivisionISOCode) {
        this.subdivisionISOCode = subdivisionISOCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Double getAccuracyRadius() {
        return accuracyRadius;
    }

    public void setAccuracyRadius(Double accuracyRadius) {
        this.accuracyRadius = accuracyRadius;
    }

    public String getAccuracyUnit() {
        return accuracyUnit;
    }

    public void setAccuracyUnit(String accuracyUnit) {
        this.accuracyUnit = accuracyUnit;
    }

    public List<Double> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = coordinates;
    }

    public String getLocationStatus() {
        return locationStatus;
    }

    public void setLocationStatus(String locationStatus) {
        this.locationStatus = locationStatus;
    }

    public Long getAdsCount() {
        return adsCount;
    }

    public void setAdsCount(Long adsCount) {
        this.adsCount = adsCount;
    }
}
