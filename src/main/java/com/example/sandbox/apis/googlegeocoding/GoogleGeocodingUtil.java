package com.example.sandbox.apis.googlegeocoding;

import com.example.sandbox.apis.googlegeocoding.models.AddressComponents;
import com.example.sandbox.apis.googlegeocoding.models.GeocodeContent;
import com.example.sandbox.apis.googlegeocoding.models.Result;
import com.example.sandbox.constants.LocationConstants;
import com.example.sandbox.constants.StatusConstants;
import com.example.sandbox.constants.TextConstants;
import com.example.sandbox.models.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 * Google Geocode realization
 */
public class GoogleGeocodingUtil {
 /*
  * Geocode request GEOCODE_URL. Here see we are passing "json" it means we will get
  * the output in JSON format. You can also pass "xml" instead of "json" for
  * XML output. For XML output GEOCODE_URL will be
  * "http://maps.googleapis.com/maps/api/geocode/xml";
  */

    public static final String GEOCODE_URL = "http://maps.googleapis.com/maps/api/geocode/json";
    private static final Logger logger = LoggerFactory.getLogger(GoogleGeocodingUtil.class);

    /*
     * Here the fullAddress String is in format like
     * "address,city,state,zipcode". Here address means "street number + route"
     * .
     */
    public static Result convertToLatLon(String fullAddress) {

  /*
   * Create an java.net.GEOCODE_URL object by passing the request GEOCODE_URL in
   * constructor. Here you can see I am converting the fullAddress String
   * in UTF-8 format. You will get Exception if you don't convert your
   * address in UTF-8 format. In parameter we also need to pass "sensor"
   * parameter. sensor (required parameter) — Indicates whether or not
   * the geocoding request comes from a device with a location sensor.
   * This value must be either true or false.
   */
        URL url = null;
        Result response = new Result();
        try {
            url = new URL(GEOCODE_URL + "?address=" + URLEncoder.encode(fullAddress, "UTF-8") + "&sensor=false");
            URLConnection conn = url.openConnection();
            // Open the Connection
            InputStream in = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            response = mapper.readValue(in, Result.class);
            in.close();
        } catch (IOException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }

        return response;
    }

    public static Result convertFromLatLon(String latlongString) {

  /*
   * Create an java.net.GEOCODE_URL object by passing the request GEOCODE_URL in
   * constructor. Here you can see I am converting the fullAddress String
   * in UTF-8 format. You will get Exception if you don't convert your
   * address in UTF-8 format. In parameter we also need to pass "sensor"
   * parameter. sensor (required parameter) — Indicates whether or not
   * the geocoding request comes from a device with a location sensor.
   * This value must be either true or false.
   */
        Result result = null;
        try {
            URL url = new URL(GEOCODE_URL + "?latlng=" + URLEncoder.encode(latlongString, "UTF-8") + "&sensor=false");
            URLConnection urlConnection = url.openConnection();
            // Open the Connection
            InputStream inputStream = urlConnection.getInputStream();
            ObjectMapper objectMapper = new ObjectMapper();
            result = objectMapper.readValue(inputStream, Result.class);
            inputStream.close();
        } catch (IOException e) {
            logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
            logger.error(TextConstants.LOG_EXCEPTION, e.toString(), e);
        }
        return result;
    }

    public static Location addressFromCoordinates(String latLon) {
        Location location = new Location();
        Result result = convertFromLatLon(latLon);
        if (result != null) {
            String place = null;
            String county = null;
            String city = null;
            String state = null;
            String town = null;
            String village = null;
            String ward = null;
            String taluk = null;
            String country = null;
            if (result.getStatus().equals(StatusConstants.OK)) {
                for (GeocodeContent geocodeContent : result.getResults()) {
                    if (!geocodeContent.getAddress_components().isEmpty()) {
                        for (AddressComponents addressComponent : geocodeContent.getAddress_components()) {
                            if (addressComponent.getLong_name() != null) {
                                List<String> type = addressComponent.getTypes();
                                if (type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_1_FILTER))
                                    state = addressComponent.getLong_name();
                                else if (type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_2_FILTER))
                                    county = addressComponent.getLong_name();
                                else if (type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_3_FILTER))
                                    town = addressComponent.getLong_name();
                                else if (type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_4_FILTER))
                                    taluk = addressComponent.getLong_name();
                                else if (type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_5_FILTER))
                                    village = addressComponent.getLong_name();
                                else if (type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_6_FILTER))
                                    ward = addressComponent.getLong_name();
                                else if (type.equals(LocationConstants.LOCALITY_FILTER))
                                    city = addressComponent.getLong_name();
                                else if (type.equals(LocationConstants.SUB_LOCALITY_LEVEL_1_FILTER))
                                    place = addressComponent.getLong_name();
                                else if (type.equals(LocationConstants.SUB_LOCALITY_LEVEL_2_FILTER))
                                    place = addressComponent.getLong_name();

//                                if (type.equals(LOCALITY_FILTER)
////                                    || type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_6_FILTER)
////                                    || type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_5_FILTER)
////                                    || type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_4_FILTER)
////                                    || type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_3_FILTER)
////                                    || type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_2_FILTER)
////                                    || type.equals(LocationConstants.ADMINISTRATIVE_AREA_LEVEL_1_FILTER)
//                                        || type.equals(SUB_LOCALITY_LEVEL_2_FILTER)
//                                        || type.equals(SUB_LOCALITY_LEVEL_1_FILTER)) {
//                                    place = addressComponent.getLong_name();
//
//                                }
                                if (addressComponent.getTypes().equals(LocationConstants.COUNTRY_FILTER)) {
                                    country = addressComponent.getLong_name();
                                }
                            }
                        }

                        if (city == null) {
                            if (place == null) {
                                if (state == null) {
                                    if (county == null) {
                                        if (town == null) {
                                            if (taluk == null) {
                                                if (village == null) {
                                                    if (ward == null) {
                                                    } else place = ward;
                                                } else place = village;
                                            } else place = taluk;
                                        } else place = town;
                                    } else place = county;
                                } else place = null;
                            }
                        } else place = city;


                        location.setCity(place);
                        location.setCountry(country);
                        if (location.getCity() != null && location.getCountry() != null) {
                            return location;
                        }
                    }
                }
            }
        }

        return location;
    }
}