package com.test.sandbox.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Collected constants of location based actions. All members of this class are immutable.
 */
public final class LocationConstants {
    public static final String M = "M";

    public static final String KM = "KM";
    public static final String DEFAULT_FLAG = "AA";
    public static final String EMPTY_DISTANCE = "";
    public static final String DEFAULT_DISTANCE = "-- M";
    public static final double EARTH_RADIUS_M = 6371000;
    public static final int EARTH_RADIUS_KM = 6371;
    public static final float ONE_DEGREE_KM = 111.12F;
    public static final float ONE_KM_IN_M = 1000F;

    public static final String PARSED_LOCATION = "\nParsed Location form IP : \n{}\n";

    public static final String COUNTRY = "country";
    public static final String LOCALITY = "locality";
    public static final String SUB_LOCALITY = "sublocality";
    public static final String SUB_LOCALITY_LEVEL_1 = "sublocality_level_1";
    public static final String SUB_LOCALITY_LEVEL_2 = "sublocality_level_2";
    public static final String NEIGHBORHOOD = "neighborhood";
    public static final String POSTAL_CODE = "postal_code";
    public static final String POLITICAL = "political";
    public static final String ADMINISTRATIVE_AREA_LEVEL_1 = "administrative_area_level_1";
    public static final String ADMINISTRATIVE_AREA_LEVEL_2 = "administrative_area_level_2";
    public static final String ADMINISTRATIVE_AREA_LEVEL_3 = "administrative_area_level_3";
    public static final String ADMINISTRATIVE_AREA_LEVEL_4 = "administrative_area_level_4";
    public static final String ADMINISTRATIVE_AREA_LEVEL_5 = "administrative_area_level_5";
    public static final String ADMINISTRATIVE_AREA_LEVEL_6 = "administrative_area_level_6";

    public static final String OK = "OK";

    public static final List<String> LOCALITY_FILTER = Collections.unmodifiableList(Arrays.asList(LOCALITY, POLITICAL));
    public static final List<String> COUNTRY_FILTER = Collections.unmodifiableList(Arrays.asList(COUNTRY, POLITICAL));
    public static final List<String> POSTAL_CODE_FILTER = Collections.unmodifiableList(Collections.singletonList(POSTAL_CODE));
    public static final List<String> SUB_LOCALITY_LEVEL_1_FILTER = Collections.unmodifiableList(Arrays.asList(SUB_LOCALITY_LEVEL_1, SUB_LOCALITY, POLITICAL));
    public static final List<String> SUB_LOCALITY_LEVEL_2_FILTER = Collections.unmodifiableList(Arrays.asList(SUB_LOCALITY_LEVEL_2, SUB_LOCALITY, POLITICAL));
    public static final List<String> NEIGHBORHOOD_FILTER = Collections.unmodifiableList(Arrays.asList(NEIGHBORHOOD, POLITICAL));
    public static final List<String> ADMINISTRATIVE_AREA_LEVEL_1_FILTER = Collections.unmodifiableList(Arrays.asList(ADMINISTRATIVE_AREA_LEVEL_1, POLITICAL));
    public static final List<String> ADMINISTRATIVE_AREA_LEVEL_2_FILTER = Collections.unmodifiableList(Arrays.asList(ADMINISTRATIVE_AREA_LEVEL_2, POLITICAL));
    public static final List<String> ADMINISTRATIVE_AREA_LEVEL_3_FILTER = Collections.unmodifiableList(Arrays.asList(ADMINISTRATIVE_AREA_LEVEL_3, POLITICAL));
    public static final List<String> ADMINISTRATIVE_AREA_LEVEL_4_FILTER = Collections.unmodifiableList(Arrays.asList(ADMINISTRATIVE_AREA_LEVEL_4, POLITICAL));
    public static final List<String> ADMINISTRATIVE_AREA_LEVEL_5_FILTER = Collections.unmodifiableList(Arrays.asList(ADMINISTRATIVE_AREA_LEVEL_5, POLITICAL));
    public static final List<String> ADMINISTRATIVE_AREA_LEVEL_6_FILTER = Collections.unmodifiableList(Arrays.asList(ADMINISTRATIVE_AREA_LEVEL_6, POLITICAL));

}
