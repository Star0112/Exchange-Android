package com.urgentrn.urncexchange.models.request;

public class LoginRequest extends UserRequest {

    private String password;
    private String refreshToken;

    public LoginRequest(String username) {
        this.login = username;
    }

    public LoginRequest(String username, String password) {
        this.login = username;
        this.password = password;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
