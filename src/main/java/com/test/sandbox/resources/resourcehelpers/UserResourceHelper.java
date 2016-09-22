package com.test.sandbox.resources.resourcehelpers;

import com.test.sandbox.constants.*;
import com.test.sandbox.daos.UserDAO;
import com.test.sandbox.models.User;
import com.test.sandbox.models.Users;
import com.test.sandbox.models.genericmodels.SessionVariables;
import com.test.sandbox.models.genericmodels.responsemodels.BaseResponse;
import com.test.sandbox.securities.AESCodec;
import com.test.sandbox.securities.PBKDF2Hash;
import com.test.sandbox.securities.SecurityUtil;
import com.test.sandbox.utilities.TimeUtil;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Helper class for UserResource class
 */
public class UserResourceHelper {

    private final Logger logger = LoggerFactory.getLogger(getClass().getName());

    private UserDAO userDAO;

    public UserResourceHelper(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Response validateUsername(User user) {
        String username = user.getUsername();
        boolean duplicateUsername = userDAO.checkDuplicateUsername(username);
        user = new User();
        Response response;
        if (!duplicateUsername) {
            user.setValidUsername(true);
            user.setStatus(StatusConstants.SUCCESS);
            user.setMessage(StatusConstants.USERNAME_NOT_EXISTS);
            response = Response.ok(user).status(Response.Status.OK).build();
        } else {
            user.setValidUsername(false);
            user.setStatus(StatusConstants.FAILURE);
            user.setMessage(StatusConstants.USERNAME_ALREADY_EXISTS);
            response = Response.ok(user).status(Response.Status.OK).build();
        }
        return response;
    }

    public Response signUpUser(User user, SessionVariables sessionVariables) {
        JSONObject jsonObject = userDAO.checkDuplicateUser(user);
        boolean duplicateUser = (boolean) jsonObject.get(ApplicationConstants.DUPLICATE_USER);
        User signUpResponse = new User();
        Response response;
        if (!duplicateUser) {
            String sessionId = sessionVariables.getSessionId();
            PBKDF2Hash pbkdf2Hash = PBKDF2Hash.getInstance();
            AESCodec aesCodec = AESCodec.getInstance();
            String salt = Arrays.toString(pbkdf2Hash.getSalt());
            String authToken = aesCodec.encryptAES(sessionId + TextConstants.STRING_FULL_COLUMN + user.getEmail() + TextConstants.STRING_FULL_COLUMN + salt + TextConstants.STRING_FULL_COLUMN);
            signUpResponse = userDAO.signUpUser(user, authToken, sessionId);
            signUpResponse.setAuthToken(null);
            signUpResponse.setValid(true);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            String userAgentType = sessionVariables.getUserAgentType();
            if (userAgentType.startsWith(SecurityConstants.APP)) {
                response = Response.ok(signUpResponse).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
            } else {
                List<NewCookie> validNewCookies = SecurityUtil.generateNewCookies(ApplicationConstants.AUTH_TOKEN, authToken);
                NewCookie newCookieRoot = validNewCookies.get(0);
                NewCookie newCookieSession = validNewCookies.get(1);
                NewCookie newCookieUser = validNewCookies.get(2);
                //NewCookie newCookieAPI = validNewCookies.get(3);
                NewCookie newAuthCookie = SecurityUtil.generateNewCookie(ApplicationConstants.AUTH_TOKEN, authToken);
                response = Response.ok(signUpResponse).status(Response.Status.OK)
                        .cookie(newCookieRoot, newCookieSession, newCookieUser)
                        .expires(expiryDate).build();
            }
        } else {
            signUpResponse.setStatus(StatusConstants.FAILURE);
            signUpResponse.setMessage((String) jsonObject.get(StatusConstants.MESSAGE));
            response = Response.ok(signUpResponse).status(Response.Status.OK).build();
        }
        return response;
    }

    public Response fetchProfile(SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        User user = new User();
        Response response;
        if (validAuthToken) {
            String authToken = sessionVariables.getAuthToken();
            user = userDAO.fetchProfile(authToken, ApplicationConstants.AUTH_TOKEN);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            response = Response.ok(user).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            user.setStatus(StatusConstants.FAILURE);
            user.setMessage(StatusConstants.FETCH_PROFILE_FAILURE);
            response = Response.ok(user).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response fetchProfile(User user, SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        String userType = sessionVariables.getUserType();
        Response response;
        if (validAuthToken) {
            String userId = user.getUserId();
            if (userId != null && !userId.isEmpty() && userType.equals(SecurityConstants.USER_TYPE_ADMIN)) {
                user = userDAO.fetchProfile(userId, ApplicationConstants.USER_ID);
                user.protectUser();
            } else if (userType.equals(SecurityConstants.USER_TYPE_USER)) {
                user.setStatus(StatusConstants.FAILURE);
                user.setMessage(StatusConstants.FETCH_PROFILE_UNAUTHORIZED);
            } else {
                user.setStatus(StatusConstants.FAILURE);
                user.setMessage(StatusConstants.FETCH_PROFILE_FAILURE);
            }
            user.setUserType(userType);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            String authToken = sessionVariables.getAuthToken();
            response = Response.ok(user).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            user.setStatus(StatusConstants.FAILURE);
            user.setMessage(StatusConstants.FETCH_PROFILE_FAILURE);
            response = Response.ok(user).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response updateProfile(User user, SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        String userType = sessionVariables.getUserType();
        BaseResponse updateProfileResponse = new BaseResponse();
        Response response;
        if (validAuthToken) {
            String authToken = sessionVariables.getAuthToken();
            if (userType.equals(SecurityConstants.USER_TYPE_ADMIN)) {
                updateProfileResponse = userDAO.updateProfile(user, authToken);
            } else if (userType.equals(SecurityConstants.USER_TYPE_USER)) {
                user.protectUserUpdate();
                updateProfileResponse = userDAO.updateProfile(user, authToken);
            }
            updateProfileResponse.setUserType(userType);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            response = Response.ok(updateProfileResponse).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            updateProfileResponse.setStatus(StatusConstants.FAILURE);
            updateProfileResponse.setMessage(StatusConstants.UPDATE_PROFILE_FAILURE);
            response = Response.ok(updateProfileResponse).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response createUser(User user, SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        String userType = sessionVariables.getUserType();
        BaseResponse baseResponse = new BaseResponse();
        Response response;
        if (validAuthToken) {
            if (userType.equals(SecurityConstants.USER_TYPE_ADMIN)) {
                JSONObject jsonObject = userDAO.checkDuplicateUser(user);
                boolean duplicateUser = (boolean) jsonObject.get(ApplicationConstants.DUPLICATE_USER);
                if (!duplicateUser) {
                    baseResponse = userDAO.createUser(user, sessionVariables.getSessionId());
                } else {
                    baseResponse.setStatus(StatusConstants.FAILURE);
                    baseResponse.setMessage((String) jsonObject.get(StatusConstants.MESSAGE));
                }
            } else if (userType.equals(SecurityConstants.USER_TYPE_USER)) {
                baseResponse.setStatus(StatusConstants.FAILURE);
                baseResponse.setMessage(StatusConstants.CREATE_USER_FAILURE);
            }
            baseResponse.setUserType(userType);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            String authToken = sessionVariables.getAuthToken();
            response = Response.ok(baseResponse).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            baseResponse.setUserType(userType);
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.CREATE_USER_FAILURE);
            response = Response.ok(baseResponse).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response readUser(User user, SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        String userType = sessionVariables.getUserType();
        Response response;
        if (validAuthToken) {
            user = userDAO.readUser(user.getUserId());
            user.setUserType(userType);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            String authToken = sessionVariables.getAuthToken();
            response = Response.ok(user).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            user.setStatus(StatusConstants.FAILURE);
            user.setMessage(StatusConstants.READ_USER_FAILURE);
            user.setUserType(userType);
            response = Response.ok(user).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response updateUser(User user, SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        String userType = sessionVariables.getUserType();
        BaseResponse baseResponse = new BaseResponse();
        Response response;
        if (validAuthToken) {
            String authToken = sessionVariables.getAuthToken();
            if (userType.equals(SecurityConstants.USER_TYPE_ADMIN)) {
                baseResponse = userDAO.updateUser(user);
            } else if (userType.equals(SecurityConstants.USER_TYPE_USER)) {
                baseResponse.setStatus(StatusConstants.FAILURE);
                baseResponse.setMessage(StatusConstants.UPDATE_USER_FAILURE);
            }
            baseResponse.setUserType(userType);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            response = Response.ok(baseResponse).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            baseResponse.setUserType(userType);
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.UPDATE_USER_FAILURE);
            response = Response.ok(baseResponse).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response deleteUser(User user, SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        String userType = sessionVariables.getUserType();
        BaseResponse baseResponse = new BaseResponse();
        Response response;
        if (validAuthToken) {
            if (userType.equals(SecurityConstants.USER_TYPE_ADMIN)) {
                baseResponse = userDAO.deleteUser(user.getUserId());
            } else if (userType.equals(SecurityConstants.USER_TYPE_USER)) {
                baseResponse.setStatus(StatusConstants.FAILURE);
                baseResponse.setMessage(StatusConstants.DELETE_USER_FAILURE);
            }
            baseResponse.setUserType(userType);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            String authToken = sessionVariables.getAuthToken();
            response = Response.ok(baseResponse).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            baseResponse.setUserType(userType);
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.DELETE_USER_FAILURE);
            response = Response.ok(baseResponse).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response deleteUsers(Users usersRequest, SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        String userType = sessionVariables.getUserType();
        BaseResponse baseResponse = new BaseResponse();
        Response response;
        if (validAuthToken) {
            if (userType.equals(SecurityConstants.USER_TYPE_ADMIN)) {
                baseResponse = userDAO.deleteUsers(usersRequest.getUserIds());
            } else if (userType.equals(SecurityConstants.USER_TYPE_USER)) {
                baseResponse.setStatus(StatusConstants.FAILURE);
                baseResponse.setMessage(StatusConstants.DELETE_USERS_FAILURE);
            }
            baseResponse.setUserType(userType);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            String authToken = sessionVariables.getAuthToken();
            response = Response.ok(baseResponse).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            baseResponse.setUserType(userType);
            baseResponse.setStatus(StatusConstants.FAILURE);
            baseResponse.setMessage(StatusConstants.DELETE_USERS_FAILURE);
            response = Response.ok(baseResponse).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response listUsers(UriInfo uriInfo, SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        String userType = sessionVariables.getUserType();
        Response response;
        Users users = new Users();
        if (validAuthToken) {
            String authToken = sessionVariables.getAuthToken();
            if (userType.equals(SecurityConstants.USER_TYPE_ADMIN)) {
                MultivaluedMap<String, String> queryParametersMultivaluedMap = uriInfo.getQueryParameters();
                List<String> sorts = queryParametersMultivaluedMap.get(PathConstants.SORT);
                List<String> orders = queryParametersMultivaluedMap.get(PathConstants.ORDER);
                String sort = queryParametersMultivaluedMap.getFirst(PathConstants.SORT);
                String type = queryParametersMultivaluedMap.getFirst(PathConstants.TYPE);
                Integer order;
                Integer start;
                Integer count;
                try {
                    order = Integer.valueOf(queryParametersMultivaluedMap.getFirst(PathConstants.ORDER));
                } catch (NumberFormatException e) {
                    logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                    order = -1;
                }
                try {
                    start = Integer.valueOf(queryParametersMultivaluedMap.getFirst(PathConstants.START));
                } catch (NumberFormatException e) {
                    logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                    start = 0;
                }
                try {
                    count = Integer.valueOf(queryParametersMultivaluedMap.getFirst(PathConstants.COUNT));
                } catch (NumberFormatException e) {
                    logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                    count = 0;
                }
                if (sort == null) {
                    sort = ApplicationConstants.DATE_ACTIVE;
                }
                users = userDAO.listUsers(authToken, start, sort, order, type, count);
            } else if (userType.equals(SecurityConstants.USER_TYPE_USER)) {
                users.setStatus(StatusConstants.FAILURE);
                users.setMessage(StatusConstants.LIST_USERS_FAILURE);
            }
            users.setUserType(userType);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            response = Response.ok(users).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            users.setUserType(userType);
            users.setStatus(StatusConstants.FAILURE);
            users.setMessage(StatusConstants.LIST_USERS_FAILURE);
            response = Response.ok(users).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response listUsers(Users users, SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        String userType = sessionVariables.getUserType();
        Response response;
        if (validAuthToken) {
            if (userType.equals(SecurityConstants.USER_TYPE_ADMIN)) {
                users = userDAO.listUsers(users.getUserIds());
            } else if (userType.equals(SecurityConstants.USER_TYPE_USER)) {
                users.setStatus(StatusConstants.FAILURE);
                users.setMessage(StatusConstants.LIST_USERS_FAILURE);
            }
            users.setUserType(userType);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            String authToken = sessionVariables.getAuthToken();
            response = Response.ok(users).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            users.setUserType(userType);
            users.setStatus(StatusConstants.FAILURE);
            users.setMessage(StatusConstants.LIST_USERS_FAILURE);
            response = Response.ok(users).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }

    public Response listLatestUsers(UriInfo uriInfo, SessionVariables sessionVariables) {
        boolean validAuthToken = sessionVariables.isValidAuthToken();
        String userType = sessionVariables.getUserType();
        Users users = new Users();
        Response response;
        if (validAuthToken) {
            String authToken = sessionVariables.getAuthToken();
            if (userType.equals(SecurityConstants.USER_TYPE_ADMIN)) {
                MultivaluedMap<String, String> queryParametersMultivaluedMap = uriInfo.getQueryParameters();
                String sort = queryParametersMultivaluedMap.getFirst(PathConstants.SORT);
                String type = queryParametersMultivaluedMap.getFirst(PathConstants.TYPE);
                Integer order;
                Integer start;
                Integer count;
                try {
                    order = Integer.valueOf(queryParametersMultivaluedMap.getFirst(PathConstants.ORDER));
                } catch (NumberFormatException e) {
                    logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                    order = -1;
                }

                try {
                    start = Integer.valueOf(queryParametersMultivaluedMap.getFirst(PathConstants.START));
                } catch (NumberFormatException e) {
                    logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                    start = 0;
                }

                try {
                    count = Integer.valueOf(queryParametersMultivaluedMap.getFirst(PathConstants.COUNT));
                } catch (NumberFormatException e) {
                    logger.error(TextConstants.LOG_EXCEPTION_MESSAGE, e.getMessage());
                    count = 0;
                }
                users = userDAO.listUsers(authToken, start, ApplicationConstants.LATEST, order, type, count);
            } else if (userType.equals(SecurityConstants.USER_TYPE_USER)) {
                users.setStatus(StatusConstants.FAILURE);
                users.setMessage(StatusConstants.LIST_USERS_FAILURE);
            }
            users.setUserType(userType);
            Date expiryDate = TimeUtil.getExpiryDate(1);
            response = Response.ok(users).status(Response.Status.OK).expires(expiryDate).header(ApplicationConstants.AUTH_TOKEN, authToken).build();
        } else {
            users.setUserType(userType);
            users.setStatus(StatusConstants.FAILURE);
            users.setMessage(StatusConstants.LIST_USERS_FAILURE);
            response = Response.ok(users).status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }
}
