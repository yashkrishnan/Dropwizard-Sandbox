package com.example.sandbox.daos;

import com.example.sandbox.models.User;
import com.example.sandbox.models.Users;
import com.example.sandbox.models.genericmodels.responsemodels.BaseResponse;
import org.json.simple.JSONObject;

import java.util.List;

/**
 * DAO interface to perform standard operations of user based actions from/to database
 */
public interface UserDAO {

    boolean checkDuplicateUsername(String username);

    JSONObject checkDuplicateUser(User user);

    User signUpUser(User user, String authToken, String sessionId);

    User fetchProfile(String value, String valueType);

    BaseResponse updateProfile(User user, String authToken);

    BaseResponse createUser(User user, String sessionId);

    User readUser(String userId);

    BaseResponse updateUser(User user);

    BaseResponse deleteUser(String userId);

    BaseResponse deleteUsers(List<String> userIds);

    Users listUsers(String authToken, Integer start, String sort, Integer order, String type, Integer count);

    Users listUsers(List<String> userIds);

    Long getUserCount(String userId, String type);
}
