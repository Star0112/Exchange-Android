package com.urgentrn.urncexchange.models.card.token;

import java.io.Serializable;

public class PlatformData implements Serializable {

    private String token;
    private String network;
    private String state;

    public String getToken() {
        return token;
    }

    public String getNetwork() {
        return network;
    }

    public String getState() {
        return state;
    }
}
