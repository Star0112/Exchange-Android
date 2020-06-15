package com.urgentrn.urncexchange.models.request;

public class PasswordConfirmRequest extends CodeRequest {

    private String username;
    private String password;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
