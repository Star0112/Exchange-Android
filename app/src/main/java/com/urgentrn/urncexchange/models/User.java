package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class User implements Serializable {

    private int id;
    private String uuid;
    private String email;
    private boolean emailConfirmed;
    private String username;
    private String phonenumber;
    private boolean phoneConfirmed;
    private int userStatus;
    private String firstname;
    private String lastname;
    private String country;
    private int tierLevel;
    private Boolean notifyWithdrawals;
    private Boolean notifyDeposits;
    private Boolean notifyLimitOrderExecution;
    private String withdrawalsBlockedUntil;
    private String withdrawalsBlockedReason;
    private boolean accountEnabled;
    private boolean exchangeEnabled;
    private boolean withdrawEnabled;
    private boolean forceResetPassword;
    private String socketToken;
    private String accessToken;

    public int getId() {
        return id;
    }

    public String getEmail() { return email; }

    public String getUsername() {
        return username;
    }

    public String getFirstname() { return firstname; }

    public String getLastname() { return lastname; }

    public String getPhonenumber() { return phonenumber; }

    public String getCountry() {
        return country;
    }

    public String getAccessToken() { return accessToken; }

    public String getSocketToken() { return socketToken; }

    public int getTierLevel() { return tierLevel; }

    public User(String email, String username, String firstname, String lastname, String phonenumber, String country) {
        this.email = email;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
        this.country = country;
    }

}
