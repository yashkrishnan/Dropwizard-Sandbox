package com.test.sandbox.utilities;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Util class for performing time based operations
 */
public class TimeUtil {
    public static Date getExpiryDate(int unitTime) {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(unitTime);
        return dateTime.toDate();
    }

    public static Date addDate(int unitTime) {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.plusDays(unitTime);
        return dateTime.toDate();
    }

    public static Date subtractDate(int unitTime) {
        DateTime dateTime = new DateTime();
        dateTime = dateTime.minusDays(unitTime);
        return dateTime.toDate();
    }
}