package com.test.sandbox.constants;

import com.test.sandbox.configurations.SandboxConfiguration;

/**
 * Collected constants of security utility. All members of this class are immutable.
 */
public final class SecurityConstants {
    public static final String AES_CODEC_ALGORITHM = "AES";
    public static final String PBKDF2_WITH_HMAC_SHA1_CODEC_ALGORITHM = "PBKDF2WithHmacSHA1";
    public static final String SHA1_PRNG_CODEC_ALGORITHM = "SHA1PRNG";

    public static final int ALGORITHM_ITERATIONS = 999;
    public static final int TOKEN_LIMIT = 50;

    public static final String D = "d";
    public static final String PERCENTAGE_ZERO = "%0";

    public static final byte[] BYTES = new byte[0];

    public static final String SESSION_ID = "sessionId";
    public static final String USER_AGENT = "User-Agent";
    public static final String ACCEPT = "Accept";
    public static final String CONTENT_TYPE = "Content-Type";

    public static final String TOKEN = "token";

    public static final String USER_TYPE_ADMIN = "admin";
    public static final String USER_TYPE_USER = "user";

    public static final String ANDROID = "Android";
    public static final String DALVIK = "Dalvik";
    public static final String I_PHONE = "iPhone";
    public static final String I_PAD = "iPad";
    public static final String OS_VERSION = "OS Version";
    public static final String USER_AGENT_ANDROID_APP = SandboxConfiguration.getAndroidUserAgent();
    public static final String USER_AGENT_IOS_APP = SandboxConfiguration.getiOSUserAgent();
    public static final String WEB = "web";
    public static final String APP = "app";
    public static final String APP_IOS = "app-ios";
    public static final String APP_ANDROID = "app-android";

    public static final int COOKIE_NEW_MAX_AGE = 100 * 60 * 24 * 90;
    public static final int COOKIE_OLD_MAX_AGE = -1;
    public static final int COOKIE_NEW_EXPIRES = 60;
    public static final int COOKIE_OLD_EXPIRES = -1;

    public static final String PAGE_NOT_FOUND = "The page you have requested is no found";
    public static final String AUTHORIZED = "Authorized";
    public static final String UNAUTHORIZED = "Unauthorized";
}