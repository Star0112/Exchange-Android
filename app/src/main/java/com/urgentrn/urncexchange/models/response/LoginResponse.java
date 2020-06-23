package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.User;

public class LoginResponse extends BaseResponse {

    private User data;

    public String getAccessToken() {
        return data.getAccessToken();
    }

    public String getSocketToken() {
        return data.getSocketToken();
    }

    public String getRefreshToken() { return ""; }

    public User getUser() {
        return data;
    }

}
