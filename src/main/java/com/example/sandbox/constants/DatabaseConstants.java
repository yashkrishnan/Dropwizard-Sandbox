package com.example.sandbox.constants;

/**
 * Collected constants of MongoDB actions. All members of this class are immutable.
 */
public final class DatabaseConstants {

    public static final String DB = "db";
    public static final String COLLECTION = "collection";
    public static final String DOCUMENT = "document";
    public static final String QUERY = "query";

    // MongoDB
    public static final String SANDBOX_DB = "sandbox";
    public static final String USERS_COLLECTION = "users";
    public static final String LOCATIONS_COLLECTION = "locations";

    public static final String _ID = "_id";
    public static final String I = "i";

    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String POINT = "Point";
    public static final String COORDINATES = "coordinates";

    public static final String MONGO_GEO_SPATIAL_INDEX_2D = "2d";
    public static final String MONGO_GEO_SPATIAL_INDEX_2D_SPHERE = "2dsphere";
    public static final String MONGO_GEO_SPATIAL_INDEX_GEO_HAY_STACK = "geoHaystack";
    public static final String MONGO_TEXT_INDEX = "text";

    public static final String MONGO_GEO_SPATIAL_INDEX_ID_2D = "geospatialIdx2D";
    public static final String MONGO_GEO_SPATIAL_INDEX_ID_2D_SPHERE = "geospatialIdx2DSphere";
    public static final String MONGO_GEO_SPATIAL_INDEX_ID_GEO_HAY_STACK = "geospatialIdxGeoHaystack";

    public static final String MONGO_GEO_INDEX_COORDINATES_2D = "coordinates_2d";
    public static final String MONGO_GEO_INDEX_COORDINATES_2DSPHERE = "coordinates_2dsphere";

    public static final String MONGO_OPERATOR_POSITIONAL = "$";
    public static final String MONGO_OPERATOR_AND = "$and";
    public static final String MONGO_OPERATOR_OR = "$or";
    public static final String MONGO_OPERATOR_NE = "$ne";
    public static final String MONGO_OPERATOR_IN = "$in";
    public static final String MONGO_OPERATOR_NIN = "$nin";
    public static final String MONGO_OPERATOR_LT = "$lt";
    public static final String MONGO_OPERATOR_GT = "$gt";
    public static final String MONGO_OPERATOR_ADD_TO_SET = "$addToSet";
    public static final String MONGO_OPERATOR_PUSH = "$push";
    public static final String MONGO_OPERATOR_PULL = "$pull";
    public static final String MONGO_OPERATOR_PULL_ALL = "$pullAll";
    public static final String MONGO_OPERATOR_SET = "$set";
    public static final String MONGO_OPERATOR_EACH = "$each";
    public static final String MONGO_OPERATOR_NATURAL = "$natural";
    public static final String MONGO_OPERATOR_GROUP = "$group";
    public static final String MONGO_OPERATOR_OPTIONS = "$options";
    public static final String MONGO_OPERATOR_REGEX = "$regex";
    public static final String MONGO_OPERATOR_TEXT = "$text";
    public static final String MONGO_OPERATOR_SEARCH = "$search";
    public static final String MONGO_OPERATOR_CASE_SENSITIVE = "$caseSensitive";
    public static final String MONGO_OPERATOR_DIACRITIC_SENSITIVE = "$diacriticSensitive";
    public static final String MONGO_OPERATOR_MATCH = "$match";
    public static final String MONGO_OPERATOR_PROJECT = "$project";
    public static final String MONGO_OPERATOR_UNWIND = "$unwind";
}
