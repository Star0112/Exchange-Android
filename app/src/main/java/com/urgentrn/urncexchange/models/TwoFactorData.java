package com.urgentrn.urncexchange.models;

public class TwoFactorData {

    private String CODE_DELIVERY_DELIVERY_MEDIUM;
    private String CODE_DELIVERY_DESTINATION;
    private String USER_ID_FOR_SRP;
    private String type;

    private String getCodeDeliveryMethod() {
        return CODE_DELIVERY_DELIVERY_MEDIUM;
    }

    private String getCodeDestination() {
        return CODE_DELIVERY_DESTINATION;
    }

    public String getUserId() {
        return USER_ID_FOR_SRP;
    }

    public String getType() {
        return type;
    }
}
