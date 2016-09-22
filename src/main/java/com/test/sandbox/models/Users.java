package com.test.sandbox.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.test.sandbox.models.genericmodels.responsemodels.BaseResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Model POJO class for actions on multiple users
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Users extends BaseResponse implements Serializable {
    private List<String> userIds;
    private List<User> users;

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
