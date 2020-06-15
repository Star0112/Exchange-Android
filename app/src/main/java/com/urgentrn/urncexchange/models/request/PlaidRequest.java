package com.urgentrn.urncexchange.models.request;

import com.urgentrn.urncexchange.models.PlaidData;

public class PlaidRequest {

    private String publicToken;
    private PlaidData data;
    private String username;

    public void setPublicToken(String publicToken) {
        this.publicToken = publicToken;
    }

    public void setData(PlaidData data) {
        this.data = data;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
