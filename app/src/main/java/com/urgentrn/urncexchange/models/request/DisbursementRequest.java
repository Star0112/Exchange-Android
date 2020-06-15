package com.urgentrn.urncexchange.models.request;

public class DisbursementRequest {

    private int asset_id;
    private double amount;
    private int account_id;
    private Integer service_id;
    private String receiver;

    public void setAssetId(int asset_id) {
        this.asset_id = asset_id;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAccountId(int account_id) {
        this.account_id = account_id;
    }

    public void setServiceId(int service_id) {
        this.service_id = service_id;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
