package com.test.sandbox.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.sandbox.models.genericmodels.responsemodels.BaseResponse;
import org.json.simple.JSONObject;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Model POJO class for user
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseResponse implements Serializable {
    private String authToken;
    private String userId;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String organisation;
    private String profileImageURL;
    private String username;
    private String password;
    private String gender;
    private String country;
    private String state;
    private String dob;
    private String planType;

    private String socketId;
    private String gcmId;
    private String apnsId;

    private String fullName;
    private String age;
    private String userStatus;
    private Boolean valid;
    private Boolean newUser;

    private String currentPassword;
    private String newPassword;

    private String passwordResetToken;
    private String verificationToken;
    private List<String> authTokens;
    private List<JSONObject> tokens;
    private String sessionId;
    private Integer signInCount;

    private transient Long lastAlertFetched;
    private transient Date planExpiry;

    private Boolean verifiedEmail;
    private Boolean verifiedPhone;
    private Boolean validUsername;
    private String searchKeyword;

    public void protectUser() {
        setAuthToken(null);
        //setUserType(null);
        setPassword(null);
        setCurrentPassword(null);
        setNewPassword(null);
        setPasswordResetToken(null);
        setVerificationToken(null);
        setAuthTokens(null);
        setTokens(null);
        setSessionId(null);
        setSignInCount(null);
        setVerifiedEmail(null);
        setVerifiedPhone(null);
        setValidUsername(null);
        setLastAlertFetched(null);
    }

    public void protectUserUpdate() {
        setAuthToken(null);
        setUserType(null);
        setUserId(null);
        setPassword(null);
        setFullName(null);
        setPasswordResetToken(null);
        setVerificationToken(null);
        setAuthTokens(null);
        setTokens(null);
        setSessionId(null);
        setSignInCount(null);
        setVerifiedEmail(null);
        setVerifiedPhone(null);
        setValidUsername(null);
        setLastAlertFetched(null);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfileImageURL() {
        return profileImageURL;
    }

    public void setProfileImageURL(String profileImageURL) {
        this.profileImageURL = profileImageURL;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getSocketId() {
        return socketId;
    }

    public void setSocketId(String socketId) {
        this.socketId = socketId;
    }

    public String getGcmId() {
        return gcmId;
    }

    public void setGcmId(String gcmId) {
        this.gcmId = gcmId;
    }

    public String getApnsId() {
        return apnsId;
    }

    public void setApnsId(String apnsId) {
        this.apnsId = apnsId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getNewUser() {
        return newUser;
    }

    public void setNewUser(Boolean newUser) {
        this.newUser = newUser;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public List<String> getAuthTokens() {
        return authTokens;
    }

    public void setAuthTokens(List<String> authTokens) {
        this.authTokens = authTokens;
    }

    public List<JSONObject> getTokens() {
        return tokens;
    }

    public void setTokens(List<JSONObject> tokens) {
        this.tokens = tokens;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getSignInCount() {
        return signInCount;
    }

    public void setSignInCount(Integer signInCount) {
        this.signInCount = signInCount;
    }

    public Long getLastAlertFetched() {
        return lastAlertFetched;
    }

    public void setLastAlertFetched(Long lastAlertFetched) {
        this.lastAlertFetched = lastAlertFetched;
    }

    public Date getPlanExpiry() {
        return planExpiry;
    }

    public void setPlanExpiry(Date planExpiry) {
        this.planExpiry = planExpiry;
    }

    public Boolean getVerifiedEmail() {
        return verifiedEmail;
    }

    public void setVerifiedEmail(Boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }

    public Boolean getVerifiedPhone() {
        return verifiedPhone;
    }

    public void setVerifiedPhone(Boolean verifiedPhone) {
        this.verifiedPhone = verifiedPhone;
    }

    public Boolean getValidUsername() {
        return validUsername;
    }

    public void setValidUsername(Boolean validUsername) {
        this.validUsername = validUsername;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
}
