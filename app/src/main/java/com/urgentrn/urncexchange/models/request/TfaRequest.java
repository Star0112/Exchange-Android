package com.urgentrn.urncexchange.models.request;

public class TfaRequest extends CodeRequest {

    private String username;
    private String session;
    private String type;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public void setType(String type) {
        this.type = type;
    }
}
