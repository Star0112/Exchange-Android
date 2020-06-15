package com.urgentrn.urncexchange.models.request;

public class UpgradeCardRequest {

    private int cardUpgradeId;
    private boolean virtual;
    private int assetId;

    public UpgradeCardRequest(int cardUpgradeId, boolean isVirtual, int assetId) {
        this.cardUpgradeId = cardUpgradeId;
        this.virtual = isVirtual;
        this.assetId = assetId;
    }
}
