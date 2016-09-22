package com.test.sandbox.utilities;


import com.test.sandbox.apis.googlegcm.models.PushNotificationMessage;
import com.test.sandbox.constants.*;
import com.test.sandbox.daos.UserDAOImpl;
import com.test.sandbox.db.mongodb.MongoAccessors;
import com.test.sandbox.models.*;
import com.test.sandbox.securities.AESCodec;
import com.test.sandbox.securities.PBKDF2Hash;
import org.json.simple.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Util class for performing bean operations
 */
public class BeanUtil {

    public static synchronized void initializeSignUpUser(User user, String authToken, String sessionId, String verificationToken) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String fullName;
        if (lastName != null && !lastName.equals(TextConstants.STRING_EMPTY_STRING)) {
            fullName = firstName + TextConstants.STRING_SPACE + lastName;
        } else {
            fullName = firstName;
        }
        user.setEmail(user.getEmail().toLowerCase());

        if (user.getUserType() == null) {
            user.setUserType(SecurityConstants.USER_TYPE_USER);
        }

        String previousUserId = MongoAccessors.getLastIssuedEntityId(DatabaseConstants.USERS_COLLECTION, ApplicationConstants.USER_ID, ApplicationConstants.DATE_CREATED);
        String userId = StringUtil.generateUniqueAlphaNumericId(previousUserId);
        user.setUserId(userId);
        user.setFullName(fullName);
        user.setSessionId(sessionId);
        user.setVerificationToken(verificationToken);
        user.setAuthTokens(Collections.singletonList(authToken));

        String socketId = user.getSocketId();
        String gcmId = user.getGcmId();
        String apnsId = user.getApnsId();
        if (socketId != null || gcmId != null || apnsId != null) {
            JSONObject token = getToken(socketId, gcmId, apnsId, authToken);
            List<JSONObject> tokens = Collections.singletonList(token);
            user.setTokens(tokens);
            user.setSocketId(null);
            user.setGcmId(null);
            user.setApnsId(null);
        }

        PBKDF2Hash pbkdf2Hash = PBKDF2Hash.getInstance();
        String password = user.getPassword();
        String dbPassword = pbkdf2Hash.generateStrongPasswordHash(password);
        user.setPassword(dbPassword);

        user.setSignInCount(1);
        if (user.getProfileImageURL() == null) {
            user.setProfileImageURL(PathConstants.DEFAULT_DP_PATH);
        }
        user.setUserStatus(StatusConstants.ACTIVE);
    }

    public static User initializeUser(User user, String sessionId) {
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String fullName;
        if (lastName != null && !lastName.equals(TextConstants.STRING_EMPTY_STRING)) {
            fullName = firstName + TextConstants.STRING_SPACE + lastName;
        } else {
            fullName = firstName;
        }
        user.setEmail(user.getEmail().toLowerCase());

        String previousUserId = MongoAccessors.getLastIssuedEntityId(DatabaseConstants.USERS_COLLECTION, ApplicationConstants.USER_ID, ApplicationConstants.DATE_CREATED);
        String userId = StringUtil.generateUniqueAlphaNumericId(previousUserId);
        user.setUserId(userId);
        user.setFullName(fullName);
        user.setUserType(SecurityConstants.USER_TYPE_USER);
        PBKDF2Hash pbkdf2Hash = PBKDF2Hash.getInstance();
        AESCodec aesCodec = AESCodec.getInstance();
        String salt = Arrays.toString(pbkdf2Hash.getSalt());
        String authToken = aesCodec.encryptAES(sessionId + TextConstants.STRING_FULL_COLUMN + user.getEmail() + TextConstants.STRING_FULL_COLUMN + salt + TextConstants.STRING_FULL_COLUMN);
        user.setVerificationToken(authToken);
        user.setAuthTokens(Collections.singletonList(authToken));
        String dbPassword = pbkdf2Hash.generateStrongPasswordHash(salt);
        user.setPassword(dbPassword);
        user.setLastAlertFetched(new Date().getTime());

        user.setSessionId(sessionId);
        user.setSignInCount(0);
        if (user.getProfileImageURL() == null) {
            user.setProfileImageURL(PathConstants.DEFAULT_DP_PATH);
        }
        user.setUserStatus(StatusConstants.ACTIVE);
        return user;
    }

    public static void initializeLocation(Location location) {
        String previousUserId = MongoAccessors.getLastIssuedEntityId(DatabaseConstants.LOCATIONS_COLLECTION, ApplicationConstants.LOCATION_ID, ApplicationConstants.DATE_CREATED);
        String locationId = StringUtil.generateUniqueAlphaNumericId(previousUserId);
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        List<Double> coordinates = Arrays.asList(longitude, latitude);
        location.setCoordinates(coordinates);
        location.setLocationId(locationId);
        location.setLocationStatus(StatusConstants.ACTIVE);
    }

    @SuppressWarnings("unchecked")
    public static JSONObject getToken(String socketId, String gcmId, String apnsId, String authToken) {
        JSONObject token = new JSONObject();
        token.put(ApplicationConstants.AUTH_TOKEN, authToken);
        if (socketId != null) {
            token.put(ApplicationConstants.TOKEN_TYPE, ApplicationConstants.SOCKET_ID);
            token.put(ApplicationConstants.TOKEN_VALUE, socketId);
            UserDAOImpl.clearExistingTokens(ApplicationConstants.SOCKET_ID, socketId);
        } else if (gcmId != null) {
            token.put(ApplicationConstants.TOKEN_TYPE, ApplicationConstants.GCM_ID);
            token.put(ApplicationConstants.TOKEN_VALUE, gcmId);
            UserDAOImpl.clearExistingTokens(ApplicationConstants.GCM_ID, gcmId);
        } else if (apnsId != null) {
            token.put(ApplicationConstants.TOKEN_TYPE, ApplicationConstants.APNS_ID);
            token.put(ApplicationConstants.TOKEN_VALUE, apnsId);
            UserDAOImpl.clearExistingTokens(ApplicationConstants.APNS_ID, apnsId);
        }
        return token;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject getToken(String tokenType, String tokenValue, String authToken) {
        JSONObject token = new JSONObject();
        token.put(ApplicationConstants.AUTH_TOKEN, authToken);
        token.put(ApplicationConstants.TOKEN_VALUE, tokenValue);
        switch (tokenType) {
            case ApplicationConstants.SOCKET_ID:
                token.put(ApplicationConstants.TOKEN_TYPE, ApplicationConstants.SOCKET_ID);
                UserDAOImpl.clearExistingTokens(ApplicationConstants.SOCKET_ID, tokenValue);
                break;
            case ApplicationConstants.GCM_ID:
                token.put(ApplicationConstants.TOKEN_TYPE, ApplicationConstants.GCM_ID);
                UserDAOImpl.clearExistingTokens(ApplicationConstants.GCM_ID, tokenValue);
                break;
            case ApplicationConstants.APNS_ID:
                token.put(ApplicationConstants.TOKEN_TYPE, ApplicationConstants.APNS_ID);
                UserDAOImpl.clearExistingTokens(ApplicationConstants.APNS_ID, tokenValue);
                break;
        }
        return token;
    }

    public static void initializeAlertMessage(PushNotificationMessage pushNotificationMessage, String target, Date date, int alertCount, Long newAlertCount) {
        pushNotificationMessage.setEventTime(date.getTime());
        pushNotificationMessage.setNewNotifications(newAlertCount);
        pushNotificationMessage.setNotificationMessage(target + NotificationConstants.NOTIFICATIONS_MESSAGE_NEW_ALERT.replace("%d", String.valueOf(alertCount)));
        pushNotificationMessage.setNotificationTitle(NotificationConstants.NOTIFICATIONS_TITLE_NEW_ALERT);
        pushNotificationMessage.setNotificationType(NotificationConstants.NOTIFICATIONS_TYPE_NEW_ALERT);
        pushNotificationMessage.setNotificationStatus(NotificationConstants.NOTIFICATIONS_STATUS_NEW);
    }
}