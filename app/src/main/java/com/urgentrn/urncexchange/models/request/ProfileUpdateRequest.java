package com.urgentrn.urncexchange.models.request;

public class ProfileUpdateRequest {
    private String firstname;
    private String lastname;
    private String phonenumber;

    public ProfileUpdateRequest(String firstname, String lastname, String phonenumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phonenumber = phonenumber;
    }
}
