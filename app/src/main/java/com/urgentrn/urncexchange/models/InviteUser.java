package com.urgentrn.urncexchange.models;

import java.io.Serializable;

public class InviteUser implements Serializable {
    private String id;
    private String code;
    private String expiration;
    private Boolean confirmed;
    private Boolean confirmedFee;
    private Boolean confirmedOrderFee;
    private int refereeId;
    private String userId;
    private Boolean isDeleted;
    private String refereeEmail;
    private String createdAt;
    private String updatedAt;

    public String getRefereeEmail() {
        return this.refereeEmail;
    }
    public String getDate() {
        return this.updatedAt;
    }
}
