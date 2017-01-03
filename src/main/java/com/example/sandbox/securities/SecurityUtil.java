package com.example.sandbox.securities;

import com.example.sandbox.configurations.SandboxConfiguration;
import com.example.sandbox.constants.ApplicationConstants;
import com.example.sandbox.constants.PathConstants;
import com.example.sandbox.constants.SecurityConstants;
import com.example.sandbox.constants.TextConstants;
import com.example.sandbox.daos.CredentialDAO;
import com.example.sandbox.utilities.TimeUtil;

import javax.ws.rs.core.NewCookie;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Utility helper class for handling security based operations
 */
@SuppressWarnings({"unused"})
public class SecurityUtil {
    public static NewCookie generateNewCookie(String name, String value) {
        javax.ws.rs.core.Cookie cookie = new javax.ws.rs.core.Cookie(name, value);
        // 2 Months Expiry date for cookie
        Date expiryDate = TimeUtil.getExpiryDate(SecurityConstants.COOKIE_NEW_EXPIRES);
        int maxAge = SecurityConstants.COOKIE_NEW_MAX_AGE;
        // NewCookie(Cookie cookie, comment, int maxAge, java.util.Date expiry, boolean secure, boolean httpOnly)
        return new NewCookie(cookie, ApplicationConstants.AUTH_TOKEN, maxAge, expiryDate, false, false);
    }

    public static List<NewCookie> generateNewCookies(String name, String value) {
        // Invalid Expiry date for cookie
        Date expiryDate = TimeUtil.getExpiryDate(SecurityConstants.COOKIE_OLD_EXPIRES);
        int maxAge = SecurityConstants.COOKIE_NEW_MAX_AGE;
        String comment = SecurityConstants.AUTHORIZED;
        javax.ws.rs.core.Cookie cookie = new javax.ws.rs.core.Cookie(name, value);
        // NewCookie(Cookie cookie, comment, int maxAge, java.util.Date expiry, boolean secure, boolean httpOnly)
        NewCookie rootNewCookie = new NewCookie(cookie, ApplicationConstants.AUTH_TOKEN, maxAge, expiryDate, false, false);
        // NewCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly)
        NewCookie sessionNewCookie = new NewCookie(name, value, PathConstants.PATH_SESSION_ROOT, PathConstants.DOMAIN, 1, comment, maxAge, expiryDate, false, false);
        // NewCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly)
        NewCookie userNewCookie = new NewCookie(name, value, PathConstants.PATH_USER_ROOT, PathConstants.DOMAIN, 1, comment, maxAge, expiryDate, false, false);
        // NewCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly)
        //NewCookie apiNewCookie = new NewCookie(name, value, PathConstants.PATH_API_ROOT, PathConstants.DOMAIN, 1, comment, maxAge, expiryDate, false, true);
        return Arrays.asList(rootNewCookie, sessionNewCookie, userNewCookie);//, apiNewCookie);
    }

    public static List<NewCookie> invalidateNewCookies(String name) {
        // Invalid Expiry date for cookie
        Date expiryDate = TimeUtil.getExpiryDate(SecurityConstants.COOKIE_OLD_EXPIRES);
        int maxAge = SecurityConstants.COOKIE_OLD_MAX_AGE;
        String comment = SecurityConstants.UNAUTHORIZED;
        javax.ws.rs.core.Cookie cookie = new javax.ws.rs.core.Cookie(name, null);
        // NewCookie(Cookie cookie, comment, int maxAge, java.util.Date expiry, boolean secure, boolean httpOnly)
        NewCookie rootNewCookie = new NewCookie(cookie, ApplicationConstants.AUTH_TOKEN, maxAge, expiryDate, false, false);
        // NewCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly)
        NewCookie sessionNewCookie = new NewCookie(name, null, PathConstants.PATH_SESSION_ROOT, PathConstants.DOMAIN, 0, comment, maxAge, expiryDate, false, false);
        // NewCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly)
        NewCookie userNewCookie = new NewCookie(name, null, PathConstants.PATH_USER_ROOT, PathConstants.DOMAIN, 0, comment, maxAge, expiryDate, false, false);
        // NewCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly)
        //NewCookie apiNewCookie = new NewCookie(name, null, PathConstants.PATH_API_ROOT, PathConstants.DOMAIN, 0, comment, maxAge, expiryDate, false, true);
        return Arrays.asList(rootNewCookie, sessionNewCookie, userNewCookie);//, apiNewCookie);
    }

    // Creates a unique password reset REST end point
    public static String generatePasswordResetURL(String email, String sessionId) {
        AESCodec aesCodec = AESCodec.getInstance();
        PBKDF2Hash pbkdf2Hash = PBKDF2Hash.getInstance();
        String salt = Arrays.toString(pbkdf2Hash.getSalt());
        String passwordResetToken = aesCodec.encryptAES(sessionId + TextConstants.STRING_FULL_COLUMN + email + TextConstants.STRING_FULL_COLUMN + salt + TextConstants.STRING_FULL_COLUMN);
        String passwordResetURL = SandboxConfiguration.getServerAddress() + PathConstants.PATH_RESET_PASSWORD + "?token=" + passwordResetToken;
        CredentialDAO.updatePasswordResetToken(email, passwordResetToken);
        return passwordResetURL;
    }

    public static NewCookie invalidateNewCookieRoot(String name) {
        javax.ws.rs.core.Cookie cookie = new javax.ws.rs.core.Cookie(name, null);
        // Invalid Expiry date for cookie
        Date expiryDate = TimeUtil.getExpiryDate(SecurityConstants.COOKIE_OLD_EXPIRES);
        int maxAge = SecurityConstants.COOKIE_OLD_MAX_AGE;
        // NewCookie(Cookie cookie, comment, int maxAge, java.util.Date expiry, boolean secure, boolean httpOnly)
        return new NewCookie(cookie, ApplicationConstants.AUTH_TOKEN, maxAge, expiryDate, false, true);
    }

    public static NewCookie invalidateNewCookieSession(String name) {
        // Invalid Expiry date for cookie
        Date expiryDate = TimeUtil.getExpiryDate(SecurityConstants.COOKIE_OLD_EXPIRES);
        String comment = SecurityConstants.UNAUTHORIZED;
        int maxAge = SecurityConstants.COOKIE_OLD_MAX_AGE;
        // NewCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly)
        return new NewCookie(name, null, PathConstants.PATH_SESSION_ROOT, PathConstants.DOMAIN, 0, comment, maxAge, expiryDate, false, true);
    }

    public static NewCookie invalidateNewCookieUser(String name) {
        // Invalid Expiry date for cookie
        Date expiryDate = TimeUtil.getExpiryDate(SecurityConstants.COOKIE_OLD_EXPIRES);
        String comment = SecurityConstants.UNAUTHORIZED;
        int maxAge = SecurityConstants.COOKIE_OLD_MAX_AGE;
        // NewCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly)
        return new NewCookie(name, null, PathConstants.PATH_USER_ROOT, PathConstants.DOMAIN, 0, comment, maxAge, expiryDate, false, true);
    }

    /*public static NewCookie invalidateNewCookieAPI(String name) {
        // Invalid Expiry date for cookie
        Date expiryDate = TimeUtil.getExpiryDate(SecurityConstants.COOKIE_OLD_EXPIRES);
        String comment = SecurityConstants.UNAUTHORIZED;
        int maxAge = SecurityConstants.COOKIE_OLD_MAX_AGE;
        // NewCookie(String name, String value, String path, String domain, String comment, int maxAge, boolean secure, boolean httpOnly)
        return new NewCookie(name, null, PathConstants.PATH_API_ROOT, PathConstants.DOMAIN, 0, comment, maxAge, expiryDate, false, true);
    }*/
}