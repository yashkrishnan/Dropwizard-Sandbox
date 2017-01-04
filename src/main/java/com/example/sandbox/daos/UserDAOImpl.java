package com.example.sandbox.daos;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.BasicDBObject;
import com.ocpsoft.pretty.time.PrettyTime;
import com.example.sandbox.constants.*;
import com.example.sandbox.db.mongodb.MongoAccessors;
import com.example.sandbox.io.FileUtil;
import com.example.sandbox.models.User;
import com.example.sandbox.models.Users;
import com.example.sandbox.models.genericmodels.responsemodels.BaseResponse;
import com.example.sandbox.securities.PBKDF2Hash;
import com.example.sandbox.utilities.BeanUtil;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


/**
 * DAO class to perform standard operations of user based actions from/to database
 */
public class UserDAOImpl implements UserDAO {

    public static void updateFileURL(String authToken, String fileURL) {
        String oldFileURL = null;
        BasicDBObject query = new BasicDBObject(ApplicationConstants.AUTH_TOKENS, new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_IN, Collections.singletonList(authToken)));
        Document document = MongoAccessors.fetchDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, query, new BasicDBObject(ApplicationConstants.PASSWORD, false));
        if (document != null) {
            oldFileURL = document.getString(ApplicationConstants.PROFILE_IMAGE_URL);
        }
        if (oldFileURL != null && !oldFileURL.equals(PathConstants.DEFAULT_DP_PATH)) {
            FileUtil.getInstance().deleteFile(oldFileURL);
        }
        BasicDBObject basicDBObject = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_SET, new BasicDBObject().append(ApplicationConstants.PROFILE_IMAGE_URL, fileURL));
        MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, query, basicDBObject);
    }

    public static Long getLastAlertFetched(String userId) {
        BasicDBObject queryObject = new BasicDBObject(ApplicationConstants.USER_ID, userId);
        Document document = MongoAccessors.fetchDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, queryObject, new BasicDBObject(ApplicationConstants.PASSWORD, false));
        Long lastAlertFetched = null;
        if (document != null) {
            lastAlertFetched = document.getLong(ApplicationConstants.LAST_ALERT_FETCHED);
        }
        return lastAlertFetched;
    }

    public static Long updateLastAlertFetched(String userId) {
        BasicDBObject query = new BasicDBObject(ApplicationConstants.USER_ID, userId);
        BasicDBObject updateData = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_SET, new BasicDBObject(ApplicationConstants.LAST_ALERT_FETCHED, new Date().getTime()));
        return MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, query, updateData);
    }

    public static void clearExistingTokens(String tokenType, String token) {
        String tokenTypeKey = ApplicationConstants.TOKENS + TextConstants.STRING_PERIOD + ApplicationConstants.TOKEN_TYPE;
        String tokenValueKey = ApplicationConstants.TOKENS + TextConstants.STRING_PERIOD + ApplicationConstants.TOKEN_VALUE;
        BasicDBObject queryObject = new BasicDBObject(tokenTypeKey, tokenType).append(tokenValueKey, token);
        List<Document> documentList = MongoAccessors.fetchDocumentsFromCollection(DatabaseConstants.USERS_COLLECTION, queryObject);
        if (!documentList.isEmpty()) {
            for (Document ignored : documentList) {
                BasicDBObject updateObject = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_PULL, new BasicDBObject(ApplicationConstants.TOKENS, new BasicDBObject(ApplicationConstants.TOKEN_VALUE, token)));
                MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, queryObject, updateObject);
            }
        }
    }

    public static List<String> readUserTokens(String userId, String tokenType) {
        String tokenTypeKey = ApplicationConstants.TOKENS + TextConstants.STRING_PERIOD + ApplicationConstants.TOKEN_TYPE;
        String tokenValueKey = ApplicationConstants.TOKENS + TextConstants.STRING_PERIOD + ApplicationConstants.TOKEN_VALUE;
        BasicDBObject queryObject = new BasicDBObject(tokenTypeKey, tokenType).append(ApplicationConstants.USER_ID, userId);
        BasicDBObject projectionObject = new BasicDBObject(tokenValueKey, 1).append(DatabaseConstants._ID, 0);
        List<Document> documentList = MongoAccessors.fetchDocumentsFromCollection(DatabaseConstants.USERS_COLLECTION, queryObject, null, projectionObject, null, null);
        List<String> userTokens = new ArrayList<>();
        if (!documentList.isEmpty()) {
            for (Document document : documentList) {
                Object tokensObject = document.get(ApplicationConstants.TOKENS);
                if (tokensObject != null) {
                    Gson gson = new Gson();
                    String tokensObjectJson = gson.toJson(tokensObject);
                    Type listType = new TypeToken<List<JSONObject>>() {
                    }.getType();
                    List<JSONObject> tokens = gson.fromJson(tokensObjectJson, listType);
                    for (JSONObject tempToken : tokens) {
                        String userToken = (String) tempToken.get(ApplicationConstants.TOKEN_VALUE);
                        userTokens.add(userToken);
                    }
                }
            }
        }
        return userTokens;
    }

    public static void addNewToken(String userId, JSONObject tokenObject) {
        BasicDBObject queryObject = new BasicDBObject(ApplicationConstants.USER_ID, userId);
        BasicDBObject updateObject = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_ADD_TO_SET, new BasicDBObject(ApplicationConstants.TOKENS, tokenObject));
        MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, queryObject, updateObject);
    }

    @Override
    public boolean checkDuplicateUsername(String username) {
        BasicDBObject query = new BasicDBObject(ApplicationConstants.USERNAME, username);
        return MongoAccessors.checkDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, query);
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject checkDuplicateUser(User user) {
        String email = user.getEmail();
        String phone = user.getPhone();
        String username = user.getUsername();
        List<BasicDBObject> queryObjectList = new ArrayList<>();
        if (email != null) {
            queryObjectList.add(new BasicDBObject(ApplicationConstants.EMAIL, email));
        }
        if (phone != null) {
            queryObjectList.add(new BasicDBObject(ApplicationConstants.PHONE, phone));
        }
        if (username != null) {
            queryObjectList.add(new BasicDBObject(ApplicationConstants.USERNAME, username));
        }

        BasicDBObject queryObject = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_OR, queryObjectList);
        List<Document> documentList = MongoAccessors.fetchDocumentsFromCollection(DatabaseConstants.USERS_COLLECTION, queryObject);
        JSONObject jsonObject = new JSONObject();
        boolean emailFound = false, phoneFound = false, usernameFound = false;
        if (!documentList.isEmpty()) {
            jsonObject.put(ApplicationConstants.DUPLICATE_USER, true);
            List<String> messages = new ArrayList<>();
            for (Document document : documentList) {
                if (email != null) {
                    String dbEmail = document.getString(ApplicationConstants.EMAIL);
                    if (dbEmail != null) {
                        if (dbEmail.equals(email)) {
                            emailFound = true;
                            messages.add(StatusConstants.EMAIL_ALREADY_EXISTS);
                        }
                    }
                }
                if (phone != null) {
                    String dbPhone = document.getString(ApplicationConstants.PHONE);
                    if (dbPhone != null) {
                        if (dbPhone.equals(phone)) {
                            phoneFound = true;
                            messages.add(StatusConstants.PHONE_ALREADY_EXISTS);
                        }
                    }
                }
                if (username != null) {
                    String dbUsername = document.getString(ApplicationConstants.USERNAME);
                    if (dbUsername != null) {
                        if (dbUsername.equals(username)) {
                            usernameFound = true;
                            messages.add(StatusConstants.USERNAME_ALREADY_EXISTS);
                        }
                    }
                }
            }
            if (messages.size() == 3) {
                jsonObject.put(StatusConstants.MESSAGE, StatusConstants.EMAIL_PHONE_USERNAME_ALREADY_EXISTS);
            } else if (messages.size() == 2) {
                if (emailFound && phoneFound) {
                    jsonObject.put(StatusConstants.MESSAGE, StatusConstants.EMAIL_PHONE_ALREADY_EXISTS);
                } else if (emailFound && usernameFound) {
                    jsonObject.put(StatusConstants.MESSAGE, StatusConstants.EMAIL_USERNAME_ALREADY_EXISTS);
                } else if (phoneFound && usernameFound) {
                    jsonObject.put(StatusConstants.MESSAGE, StatusConstants.PHONE_USERNAME_ALREADY_EXISTS);
                }
            } else {
                jsonObject.put(StatusConstants.MESSAGE, messages.get(0));
            }
        } else {
            jsonObject.put(ApplicationConstants.DUPLICATE_USER, false);
        }
        return jsonObject;
    }

    @Override
    public User signUpUser(User user, String authToken, String sessionId) {
        Gson gson = new Gson();
        BeanUtil.initializeSignUpUser(user, authToken, sessionId, authToken);
        Date date = new Date();
        String dbJSON = gson.toJson(user);
        Document document = Document.parse(dbJSON)
                .append(ApplicationConstants.DATE_CREATED, date)
                .append(ApplicationConstants.DATE_ACTIVE, date)
                .append(ApplicationConstants.LAST_ALERT_FETCHED, new Date().getTime());
        ObjectId objectId = MongoAccessors.insertDocumentToCollection(DatabaseConstants.USERS_COLLECTION, document);
        if (objectId != null) {
            user = gson.fromJson(dbJSON, User.class);
            PrettyTime prettyTime = new PrettyTime();
            user.setActiveSince(prettyTime.format(date));
            user.setLastActive(prettyTime.format(date));
            user.protectUser();
            user.setStatus(StatusConstants.SUCCESS);
            user.setMessage(StatusConstants.SIGN_UP_SUCCESS);
        } else {
            user.protectUser();
            user.setStatus(StatusConstants.FAILURE);
            user.setMessage(StatusConstants.SIGN_UP_FAILURE);
        }
        return user;
    }

    @Override
    public User fetchProfile(String value, String valueType) {
        BasicDBObject query = null;
        if (valueType.equals(ApplicationConstants.AUTH_TOKEN)) {
            query = new BasicDBObject(ApplicationConstants.AUTH_TOKENS, new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_IN, Collections.singletonList(value)));
        } else if (valueType.equals(ApplicationConstants.USER_ID)) {
            query = new BasicDBObject(ApplicationConstants.USER_ID, value);
        }
        Document document = MongoAccessors.fetchDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, query, new BasicDBObject(ApplicationConstants.PASSWORD, false));
        User user = new User();
        if (document != null) {

            String documentJSON = document.toJson();
            user = new Gson().fromJson(documentJSON, User.class);
            user.protectUser();
            Date accountCreated = document.getDate(ApplicationConstants.DATE_CREATED);
            Date lastActive = document.getDate(ApplicationConstants.DATE_ACTIVE);
            PrettyTime prettyTime = new PrettyTime();
            user.setActiveSince(prettyTime.format(accountCreated));
            user.setLastActive(prettyTime.format(lastActive));
            user.setStatus(StatusConstants.SUCCESS);
            user.setMessage(StatusConstants.FETCH_PROFILE_SUCCESS);
        } else {
            user.protectUser();
            user.setStatus(StatusConstants.FAILURE);
            user.setMessage(StatusConstants.FETCH_PROFILE_FAILURE);
        }
        return user;
    }

    @Override
    public BaseResponse updateProfile(User user, String authToken) {
        String currentPasswordInRequest = user.getCurrentPassword();
        String newPassword = user.getNewPassword();
        BaseResponse updateProfileResponse = new BaseResponse();
        if (currentPasswordInRequest != null && newPassword != null) {
            BasicDBObject query = new BasicDBObject(ApplicationConstants.AUTH_TOKENS, new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_IN, Collections.singletonList(authToken)));
            Document document = MongoAccessors.fetchDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, query);
            if (document != null) {
                String currentPasswordInDB = (String) document.get(ApplicationConstants.PASSWORD);
                PBKDF2Hash pbkdf2Hash = PBKDF2Hash.getInstance();
                boolean passwordsMatched = false;
                if (currentPasswordInDB != null) {
                    passwordsMatched = pbkdf2Hash.validateStrongHash(currentPasswordInRequest, currentPasswordInDB);
                }
                if (passwordsMatched) {
                    user.setPassword(pbkdf2Hash.generateStrongHash(newPassword));
                    user.setCurrentPassword(null);
                    user.setNewPassword(null);

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
                        user.setTokens(Collections.singletonList(token));
                    } else {
                        user.setTokens(new ArrayList<JSONObject>());
                    }
                    user.setAuthTokens(Collections.singletonList(authToken));
                } else {
                    updateProfileResponse.setStatus(StatusConstants.FAILURE);
                    updateProfileResponse.setMessage(StatusConstants.UPDATE_PROFILE_FAILURE);
                    return updateProfileResponse;
                }
            }
        }
        user.setCurrentPassword(null);
        user.setNewPassword(null);

        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String fullName;
        if (lastName != null && !lastName.equals(TextConstants.STRING_EMPTY_STRING)) {
            fullName = firstName + TextConstants.STRING_SPACE + lastName;
        } else {
            fullName = firstName;
        }
        user.setFullName(fullName);
        BasicDBObject query = new BasicDBObject(ApplicationConstants.AUTH_TOKENS, new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_IN, Collections.singletonList(authToken)));
        String requestJSON = new Gson().toJson(user);
        BasicDBObject updateProfileObject = (BasicDBObject) com.mongodb.util.JSON.parse(requestJSON);
        BasicDBObject updateObject = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_SET, updateProfileObject);
        long status = MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, query, updateObject);

        if (status != 0) {
            updateProfileResponse.setStatus(StatusConstants.SUCCESS);
            updateProfileResponse.setMessage(StatusConstants.UPDATE_PROFILE_SUCCESS);
        } else {
            updateProfileResponse.setStatus(StatusConstants.FAILURE);
            updateProfileResponse.setMessage(StatusConstants.UPDATE_PROFILE_FAILURE);
        }
        return updateProfileResponse;
    }

    @Override
    public BaseResponse createUser(User user, String sessionId) {
        user = BeanUtil.initializeUser(user, sessionId);
        String userJSON = new Gson().toJson(user);
        Document document = Document.parse(userJSON)
                .append(ApplicationConstants.DATE_CREATED, new Date())
                .append(ApplicationConstants.DATE_ACTIVE, new Date());
        ObjectId objectId = MongoAccessors.insertDocumentToCollection(DatabaseConstants.USERS_COLLECTION, document);
        BaseResponse baseResponse = new BaseResponse();
        if (objectId != null) {
            baseResponse.setStatus(StatusConstants.SUCCESS);
            baseResponse.setMessage(StatusConstants.CREATE_USER_SUCCESS);
        } else {
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.CREATE_USER_FAILURE);
        }
        return baseResponse;
    }

    @Override
    public User readUser(String userId) {
        BasicDBObject queryObject = new BasicDBObject(ApplicationConstants.USER_ID, userId);
        Document document = MongoAccessors.fetchDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, queryObject, new BasicDBObject(ApplicationConstants.PASSWORD, false));
        User user = new User();
        if (document != null) {
            Gson gson = new Gson();
            user = gson.fromJson(gson.toJson(document), User.class);
            user.protectUser();
            Date userCreated = document.getDate(ApplicationConstants.DATE_CREATED);
            Date lastActive = document.getDate(ApplicationConstants.DATE_ACTIVE);
            PrettyTime prettyTime = new PrettyTime();
            user.setActiveSince(prettyTime.format(userCreated));
            user.setLastActive(prettyTime.format(lastActive));
            user.setStatus(StatusConstants.SUCCESS);
            user.setMessage(StatusConstants.READ_USER_SUCCESS);
        } else {
            user.protectUser();
            user.setStatus(StatusConstants.FAILURE);
            user.setMessage(StatusConstants.READ_USER_FAILURE);
        }
        return user;
    }

    @Override
    public BaseResponse updateUser(User user) {
        String currentPasswordInRequest = user.getCurrentPassword();
        String newPassword = user.getNewPassword();
        BasicDBObject query = new BasicDBObject(ApplicationConstants.USER_ID, user.getUserId());
        BaseResponse updateProfileResponse = new BaseResponse();
        if (currentPasswordInRequest != null && newPassword != null) {
            Document document = MongoAccessors.fetchDocumentFromCollection(DatabaseConstants.USERS_COLLECTION, query);
            if (document != null) {
                String currentPasswordInDB = (String) document.get(ApplicationConstants.PASSWORD);
                PBKDF2Hash pbkdf2Hash = PBKDF2Hash.getInstance();
                boolean passwordsMatched = false;
                if (currentPasswordInDB != null) {
                    passwordsMatched = pbkdf2Hash.validateStrongHash(currentPasswordInRequest, currentPasswordInDB);
                }
                if (passwordsMatched) {
                    user.setPassword(pbkdf2Hash.generateStrongHash(newPassword));
                    user.setAuthTokens(new ArrayList<String>());
                    user.setTokens(new ArrayList<JSONObject>());
                } else {
                    updateProfileResponse.setStatus(StatusConstants.FAILURE);
                    updateProfileResponse.setMessage(StatusConstants.UPDATE_USER_FAILURE);
                    return updateProfileResponse;
                }
            }
        }
        user.setCurrentPassword(null);
        user.setNewPassword(null);
        String userJSON = new Gson().toJson(user);
        BasicDBObject document = ((BasicDBObject) com.mongodb.util.JSON.parse(userJSON))
                .append(ApplicationConstants.DATE_ACTIVE, new Date());
        BasicDBObject updateDbObject = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_SET, document);
        long status = MongoAccessors.updateDocumentsInCollection(DatabaseConstants.USERS_COLLECTION, query, updateDbObject);
        BaseResponse baseResponse = new BaseResponse();
        if (status != 0) {
            baseResponse.setStatus(StatusConstants.SUCCESS);
            baseResponse.setMessage(StatusConstants.UPDATE_USER_SUCCESS);
        } else {
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.UPDATE_USER_FAILURE);
        }
        return baseResponse;
    }

    @Override
    public BaseResponse deleteUser(String userId) {
        BasicDBObject queryObject = new BasicDBObject(ApplicationConstants.USER_ID, userId);
        long status = MongoAccessors.removeDocumentInCollection(DatabaseConstants.USERS_COLLECTION, queryObject);
        BaseResponse baseResponse = new BaseResponse();
        if (status != 0) {
            baseResponse.setStatus(StatusConstants.SUCCESS);
            baseResponse.setMessage(StatusConstants.DELETE_USER_SUCCESS);
        } else {
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.DELETE_USER_FAILURE);
        }
        return baseResponse;
    }

    @Override
    public BaseResponse deleteUsers(List<String> userIds) {
        List<BasicDBObject> userIdBasicDBObjects = new ArrayList<>();
        for (String userId : userIds) {
            userIdBasicDBObjects.add(new BasicDBObject(ApplicationConstants.USER_ID, userId));
        }
        BasicDBObject queryObject = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_OR, userIdBasicDBObjects);
        long status = MongoAccessors.removeDocumentInCollection(DatabaseConstants.USERS_COLLECTION, queryObject);
        BaseResponse baseResponse = new BaseResponse();
        if (status != 0) {
            baseResponse.setStatus(StatusConstants.SUCCESS);
            baseResponse.setMessage(StatusConstants.DELETE_USERS_SUCCESS);
        } else {
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.DELETE_USERS_FAILURE);
        }
        return baseResponse;
    }

    @Override
    public Users listUsers(String authToken, Integer start, String sort, Integer order, String type, Integer count) {
        BasicDBObject query = new BasicDBObject(ApplicationConstants.AUTH_TOKENS, new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_NIN, Collections.singletonList(authToken)));
        if (type != null && !type.equals(ApplicationConstants.ALL)) {
            query.append(ApplicationConstants.USER_TYPE, type);
        }
        BasicDBObject sortFields = new BasicDBObject(sort, order);
        if (sort.equals(ApplicationConstants.DATE_CREATED)) {
            if (count == null) {
                count = 8;
            }
        }
        List<Document> documentList = MongoAccessors.fetchDocumentsFromCollection(DatabaseConstants.USERS_COLLECTION, query, sortFields, null, start, count);
        Users users = new Users();
        if (documentList != null && !documentList.isEmpty()) {
            Gson gson = new Gson();
            String documentListJSON = gson.toJson(documentList);
            Type listType = new TypeToken<List<User>>() {
            }.getType();
            List<User> usersList = gson.fromJson(documentListJSON, listType);
            int i = 0;
            for (User user : usersList) {
                Date accountCreated = documentList.get(i).getDate(ApplicationConstants.DATE_CREATED);
                Date lastActive = documentList.get(i).getDate(ApplicationConstants.DATE_ACTIVE);
                PrettyTime prettyTime = new PrettyTime();
                user.setActiveSince(prettyTime.format(accountCreated));
                user.setLastActive(prettyTime.format(lastActive));
                user.protectUser();
                i++;
            }
            users.setUsers(usersList);
            users.setStatus(StatusConstants.SUCCESS);
            users.setMessage(StatusConstants.LIST_USERS_SUCCESS);
        } else {
            users.setStatus(StatusConstants.FAILURE);
            users.setMessage(StatusConstants.LIST_USERS_FAILURE);
        }
        return users;
    }

    @Override
    public Users listUsers(List<String> userIds) {
        List<BasicDBObject> orQueries = new ArrayList<>();
        for (String userId : userIds) {
            orQueries.add(new BasicDBObject(ApplicationConstants.USER_ID, userId));
        }
        BasicDBObject query = new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_OR, orQueries);
        List<Document> documentList = MongoAccessors.fetchDocumentsFromCollection(DatabaseConstants.USERS_COLLECTION, query, new BasicDBObject(ApplicationConstants.DATE_ACTIVE, -1), null, null, null);
        Users users = new Users();
        if (documentList != null && !documentList.isEmpty()) {
            Gson gson = new Gson();
            String documentListJSON = gson.toJson(documentList);
            Type listType = new TypeToken<List<User>>() {
            }.getType();
            List<User> usersList = gson.fromJson(documentListJSON, listType);
            int i = 0;
            for (User user : usersList) {
                user.protectUser();
                Date accountCreated = documentList.get(i).getDate(ApplicationConstants.DATE_CREATED);
                Date lastActive = documentList.get(i).getDate(ApplicationConstants.DATE_ACTIVE);
                PrettyTime prettyTime = new PrettyTime();
                user.setActiveSince(prettyTime.format(accountCreated));
                user.setLastActive(prettyTime.format(lastActive));
            }
            users.setUsers(usersList);
            users.setStatus(StatusConstants.SUCCESS);
            users.setMessage(StatusConstants.LIST_USERS_SUCCESS);
        } else {
            users.setStatus(StatusConstants.FAILURE);
            users.setMessage(StatusConstants.LIST_USERS_FAILURE);
        }
        return users;
    }

    @Override
    public Long getUserCount(String userId, String type) {
        BasicDBObject query = new BasicDBObject(ApplicationConstants.USER_ID, new BasicDBObject(DatabaseConstants.MONGO_OPERATOR_NE, Collections.singletonList(userId))).append(StatusConstants.USER_STATUS, type);
        return MongoAccessors.checkDocumentCount(DatabaseConstants.USERS_COLLECTION, query);
    }

}