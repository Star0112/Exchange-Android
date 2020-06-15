package com.urgentrn.urncexchange.models.request;

public class WithdrawRequest {

    private String address;
    private double amount;

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
