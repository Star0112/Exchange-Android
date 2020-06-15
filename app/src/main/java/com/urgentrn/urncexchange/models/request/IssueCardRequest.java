package com.urgentrn.urncexchange.models.request;

public class IssueCardRequest {

    private int cardId;
    private Boolean virtual;
    private int asset_id;

    // used to buy gift card
    private String ref;
    private Double amount;

    public IssueCardRequest(int cardId, boolean virtual, int assetId) {
        this.cardId = cardId;
        this.virtual = virtual;
        this.asset_id = assetId;
    }

    public IssueCardRequest(int cardId, int assetId, String ref, double amount) {
        this.cardId = cardId;
        this.asset_id = assetId;
        this.ref = ref;
        this.amount = amount;
    }
}
