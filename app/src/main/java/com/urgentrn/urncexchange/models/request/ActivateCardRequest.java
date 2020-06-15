package com.urgentrn.urncexchange.models.request;

public class ActivateCardRequest {

    private String reference;
    private int expiryDate;
    private String cvv;

    public ActivateCardRequest(String reference, int expiryDate, String cvv) {
        this.reference = reference;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }
}
