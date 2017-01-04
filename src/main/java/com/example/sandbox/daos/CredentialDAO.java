package com.example.sandbox.daos;

import com.example.sandbox.constants.*;
import com.example.sandbox.db.mongodb.MongoAccessors;
import com.example.sandbox.models.User;
import com.example.sandbox.models.genericmodels.responsemodels.BaseResponse;
import com.example.sandbox.securities.AESCodec;
import com.example.sandbox.securities.PBKDF2Hash;
import com.example.sandbox.utilities.BeanUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.ocpsoft.pretty.time.PrettyTime;
import org.bson.Document;
import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.util.*;

/**
 * DAO class to perform standard operations of credential based actions from/to database
 */
public class CredentialDAO {

    public static boolean validateAuthToken(String authToken) {
        BasicDBObject query = new BasicDBObject(ApplicationConstants.AUTH_TOKENS, new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_IN, Collections.singletonList(authToken)));
        Document document = MongoAccessors.fetchDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, query);
        boolean status = false;
        if (document != null) {
            status = true;
            BasicDBObject updateData = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_SET, new BasicDBObject(ApplicationConstants.DATE_ACTIVE, new Date()));
            MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, query, updateData);
        }
        return status;
    }

    @SuppressWarnings("unchecked")
    public static JSONObject validateUser(String authToken) {
        BasicDBObject query = new BasicDBObject(ApplicationConstants.AUTH_TOKENS,
                new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_IN, Collections.singletonList(authToken)));
        Document document = MongoAccessors.fetchDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, query);
        JSONObject jsonObject = new JSONObject();
        if (document != null) {
            String userId = document.getString(ApplicationConstants.USER_ID);
            jsonObject.put(ApplicationConstants.AUTH_TOKEN, authToken);
            jsonObject.put(ApplicationConstants.USER_ID, userId);
            jsonObject.put(ApplicationConstants.USER_TYPE, document.getString(ApplicationConstants.USER_TYPE));
            jsonObject.put(ApplicationConstants.VALID_AUTH_TOKEN, true);
            updateLastActive(userId);
        }
        return jsonObject;
    }

    public static void updateLastActive(String userId) {
        BasicDBObject queryObject = new BasicDBObject(ApplicationConstants.USER_ID, userId);
        BasicDBObject updateData = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_SET, new BasicDBObject(ApplicationConstants.DATE_ACTIVE, new Date()));
        MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, queryObject, updateData);
    }

    public static void updatePasswordResetToken(String email, String passwordResetToken) {
        BasicDBObject query = new BasicDBObject(ApplicationConstants.EMAIL, email);
        BasicDBObject document = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_SET,
                new BasicDBObject(ApplicationConstants.PASSWORD_RESET_TOKEN, passwordResetToken));
        MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, query, document);
    }

    public boolean validateKeyValue(String key, String value) {
        BasicDBObject query = new BasicDBObject(key, value);
        return MongoAccessors.checkDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, query);
    }

    public User signInUser(User user, String sessionId) {
        String email = user.getEmail().toLowerCase();
        String password = user.getPassword();
        user.setValid(false);
        BasicDBObject query = new BasicDBObject(ApplicationConstants.EMAIL, email);
        Document document = MongoAccessors.fetchDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, query);
        if (document != null) {
            String dbPassword = (String) document.get(ApplicationConstants.PASSWORD);
            boolean passwordsMatched = false;
            PBKDF2Hash pbkdf2Hash = PBKDF2Hash.getInstance();
            if (dbPassword != null) {
                passwordsMatched = pbkdf2Hash.validateStrongHash(password, dbPassword);
            }
            if (passwordsMatched) {
                String socketId = user.getSocketId();
                String gcmId = user.getGcmId();
                String apnsId = user.getApnsId();
                String documentJSONString = document.toJson();
                Gson gson = new Gson();
                user = gson.fromJson(documentJSONString, User.class);
                int signInCount = (int) document.get(ApplicationConstants.SIGN_IN_IN_COUNT);
                Date accountCreated = document.getDate(ApplicationConstants.DATE_CREATED);
                Date lastActive = document.getDate(ApplicationConstants.DATE_ACTIVE);
                PrettyTime prettyTime = new PrettyTime();
                user.setActiveSince(prettyTime.format(accountCreated));
                user.setLastActive(prettyTime.format(lastActive));
                signInCount += 1;
                // Update new credentials to database
                AESCodec aesCodec = AESCodec.getInstance();
                String salt = Arrays.toString(pbkdf2Hash.getSalt());
                String authToken = aesCodec.encryptAES(sessionId + TextConstants.STRING_FULL_COLUMN + email
                        + TextConstants.STRING_FULL_COLUMN + salt
                        + TextConstants.STRING_FULL_COLUMN);

                BasicDBObject updateData = new BasicDBObject(ApplicationConstants.SIGN_IN_IN_COUNT, signInCount)
                        .append(SecurityConstants.SESSION_ID, sessionId).append(ApplicationConstants.DATE_ACTIVE, new Date());

                @SuppressWarnings("unchecked")
                List<String> authTokens = (List<String>) document.get(ApplicationConstants.AUTH_TOKENS);
                authTokens.add(0, authToken);
                if (authTokens.size() >= SecurityConstants.TOKEN_LIMIT) {
                    authTokens = authTokens.subList(0, SecurityConstants.TOKEN_LIMIT);
                    updateData.append(ApplicationConstants.AUTH_TOKENS, authTokens);
                } else if (authTokens.size() < SecurityConstants.TOKEN_LIMIT) {
                    updateData.append(ApplicationConstants.AUTH_TOKENS, authTokens);
                }

                if (socketId != null || gcmId != null || apnsId != null) {
                    JSONObject token = BeanUtil.getToken(socketId, gcmId, apnsId, authToken);
                    user.setSocketId(null);
                    user.setGcmId(null);
                    user.setApnsId(null);
                    Object tokensObject = document.get(ApplicationConstants.TOKENS);
                    String tokensObjectJson = gson.toJson(tokensObject);
                    Type listType = new TypeToken<List<JSONObject>>() {
                    }.getType();
                    List<JSONObject> tokens = gson.fromJson(tokensObjectJson, listType);
                    if (tokens == null) {
                        tokens = new ArrayList<>();
                    }
                    tokens.add(0, token);
                    if (tokens.size() >= SecurityConstants.TOKEN_LIMIT) {
                        tokens = tokens.subList(0, SecurityConstants.TOKEN_LIMIT);
                        updateData.append(ApplicationConstants.TOKENS, tokens);
                    } else if (tokens.size() < SecurityConstants.TOKEN_LIMIT) {
                        updateData.append(ApplicationConstants.TOKENS, tokens);
                    }
                }
                BasicDBObject updateDbObject = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_SET, updateData);
                MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, query, updateDbObject);
                String userId = document.getString(ApplicationConstants.USER_ID);
                user.protectUser();
                user.setAuthToken(authToken);
                user.setUserId(userId);
                user.setValid(true);
                user.setNewUser(false);
            } else {
                user.setNewUser(true);
                user.setUserId(null);
                user.setValid(false);
                user.protectUser();
            }
        }
        return user;
    }

    public boolean validatePasswordResetToken(String email, String passwordResetToken) {
        // $and query items
        List<BasicDBObject> andDBObjects = Arrays.asList(new BasicDBObject(ApplicationConstants.EMAIL, email),
                new BasicDBObject(ApplicationConstants.PASSWORD_RESET_TOKEN, passwordResetToken));
        BasicDBObject query = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_AND, andDBObjects);
        return MongoAccessors.checkDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, query);
    }

    public void resetPasswordAndTokens(String email, String password) {
        BasicDBObject query = new BasicDBObject(ApplicationConstants.EMAIL, email);
        PBKDF2Hash pbkdf2Hash = PBKDF2Hash.getInstance();
        String dbPassword = pbkdf2Hash.generateStrongHash(password);
        BasicDBObject document = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_SET,
                new BasicDBObject(ApplicationConstants.PASSWORD, dbPassword)
                        .append(ApplicationConstants.PASSWORD_RESET_TOKEN, null)
                        .append(ApplicationConstants.AUTH_TOKENS, new ArrayList<>())
                        .append(ApplicationConstants.TOKENS, new ArrayList<>()));
        MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, query, document);
    }

    public BaseResponse updatePassword(User user, String authToken) {
        BasicDBObject queryObject = new BasicDBObject(ApplicationConstants.AUTH_TOKENS,
                new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_IN, Collections.singletonList(authToken)));
        Document document = MongoAccessors.fetchDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, queryObject);
        BaseResponse baseResponse = new BaseResponse();
        if (document != null) {
            String currentPasswordInDB = (String) document.get(ApplicationConstants.PASSWORD);
            String currentPasswordInRequest = user.getCurrentPassword();
            String newPassword = user.getNewPassword();
            PBKDF2Hash pbkdf2Hash = PBKDF2Hash.getInstance();
            boolean passwordsMatched = false;
            if (currentPasswordInDB != null) {
                passwordsMatched = pbkdf2Hash.validateStrongHash(currentPasswordInRequest, currentPasswordInDB);
            }
            if (passwordsMatched) {
                String dbPassword = pbkdf2Hash.generateStrongHash(newPassword);
                BasicDBObject updateObject = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_SET, new BasicDBObject(ApplicationConstants.PASSWORD, dbPassword)
                        .append(ApplicationConstants.AUTH_TOKENS, Collections.singletonList(authToken)));

                Object tokensObject = document.get(ApplicationConstants.TOKENS);
                if (tokensObject != null) {
                    Gson gson = new Gson();
                    String tokensObjectJson = gson.toJson(tokensObject);
                    Type listType = new TypeToken<List<JSONObject>>() {
                    }.getType();
                    List<JSONObject> tokens = gson.fromJson(tokensObjectJson, listType);
                    JSONObject token = null;
                    for (JSONObject tempToken : tokens) {
                        if (tempToken.get(ApplicationConstants.AUTH_TOKEN).equals(authToken)) {
                            token = tempToken;
                        }
                    }
                    if (token != null) {
                        updateObject.append(ApplicationConstants.TOKENS, Collections.singletonList(token));
                    }
                } else {
                    updateObject.append(ApplicationConstants.TOKENS, new ArrayList<>());
                }

                MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, queryObject, updateObject);
                baseResponse.setStatus(StatusConstants.SUCCESS);
                baseResponse.setMessage(StatusConstants.UPDATE_PASSWORD_SUCCESS);
            } else {
                baseResponse.setStatus(StatusConstants.FAILURE);
                baseResponse.setMessage(StatusConstants.UPDATE_PASSWORD_FAILURE);
            }
        } else {
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.UPDATE_PASSWORD_FAILURE);
        }
        return baseResponse;
    }

    // Properly logs out a signed in user and clears necessary session data gcmToken and apnsToken
    public BaseResponse logoutUser(String authToken) {
        BasicDBObject query = new BasicDBObject(ApplicationConstants.AUTH_TOKENS,
                new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_IN, Collections.singletonList(authToken)));
        BasicDBObject updateObject = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_PULL, new BasicDBObject(ApplicationConstants.AUTH_TOKENS, authToken)
                .append(ApplicationConstants.TOKENS, new BasicDBObject(ApplicationConstants.AUTH_TOKEN, authToken)));
        long updateStatus = MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, query, updateObject);
        BaseResponse baseResponse = new BaseResponse();
        if (updateStatus != 0) {
            baseResponse.setStatus(StatusConstants.SUCCESS);
            baseResponse.setMessage(StatusConstants.LOG_OUT_SUCCESS);
        } else {
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.LOG_OUT_FAILURE);
        }
        return baseResponse;
    }
}
