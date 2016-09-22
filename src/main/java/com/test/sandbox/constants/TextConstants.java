package com.test.sandbox.constants;

import java.util.regex.Pattern;

/**
 * Collected constants of regular expression constants. All members of this class are immutable.
 */
public final class TextConstants {

    public static final String STRING_SPACE = " ";
    public static final String STRING_FULL_COLUMN = ":";
    public static final String STRING_FORWARD_SLASH = "/";
    public static final String STRING_EMPTY_STRING = "";
    public static final String STRING_PERIOD = ".";
    public static final String STRING_ASTERISK = "*";
    public static final String STRING_PIPE = "|";

    public static final String ENCODING_UTF_8 = "UTF-8";
    public static final char ZERO = '0';
    public static final int ID_LENGTH = 10;
    public static final String ALPHA_NUMERIC_FIRST_ID = "0000000000";
    public static final String ALPHA_NUMERIC_LAST_ID = "ZZZZZZZZZZ";
    public static final String ALPHA_NUMERALS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOG_RESOURCE_WITH_REQUEST = "\nResource Path : {}\nHTTP Method : {}\nRequest : \n{}\nSession ID : {}\nRequest Source IP : {}\nUser Agent Type : {}\nHTTP Accept : {}\nContent Type : {}\n";
    public static final String LOG_RESOURCE_WITHOUT_REQUEST = "\nResource Path : {}\nHTTP Method : {}\nSession ID : {}\nRequest Source IP : {}\nUser Agent Type : {}\nHTTP Accept : {}\nContent Type : {}\n";
    public static final String LOG_RESPONSE = "\nResponse : \n{}\n";
    public static final String LOG_RESPONSE_AUTH_TOKEN = "\nResponse : \n{}\nAuthToken : {}\n";
    public static final String LOG_SPIDER = "\nSpider : {}\n";
    public static final String LOG_SPIDER_PROGRESS = "\nSpider progress of {} :  {} %\n";
    public static final String LOG_SPIDER_COMPLETED = "\nSpider completed for : {}\n";
    public static final String LOG_ACTIVE_SCAN = "\nActive Scan : {}\n";
    public static final String LOG_ACTIVE_SCAN_PROGRESS = "\nActive Scan progress of {} :  {} %\n";
    public static final String LOG_ACTIVE_SCAN_COMPLETED = "\nActive Scan completed for : {}\n";
    public static final String LOG_EXCEPTION = "\nException : {}\n";
    public static final String LOG_EXCEPTION_MESSAGE = "\nException Message : {}\n";
    public static final String LOG_EXCEPTION_REASON = "\nException Reason : {}\n";
    public static final String LOG_INPUT_TARGET = "\nInput target : {}\n";
    public static final String LOG_JSOUP_CONNECTING_TARGET = "\nJSOUP trying to connect to target : {}\n";
    public static final String LOG_JSOUP_CONNECTED_TARGET = "\nJSOUP connected to target : {}\n";
    public static final String LOG_PARSED_TARGET = "\nParsed target : {}\n";
    public static final String REGEX_SITE_PREFIX = "^(http://|http://www\\.|https://|https://www\\.|www\\.)";
    public static final String REGEX_UNICODE = "[^\\u0000-\\uFFFF]";
    public static final String REGEX_IPADDRESS =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    public static final String TRUE = "True";
    private static final String EMAIL_PATTERN_REGEX = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
    public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_PATTERN_REGEX);
    private static final String CONTACT_NUM_PATTERN_REGEX = "\\d{10}";
    public static final Pattern CONTACT_NUM_PATTERN = Pattern.compile(CONTACT_NUM_PATTERN_REGEX);
}
