package com.urgentrn.urncexchange.models.card.token;

public class DigitalWalletToken {

    private String tokenReferenceId;
    private String panReferenceId;

    public void setTokenReferenceId(String tokenReferenceId) {
        this.tokenReferenceId = tokenReferenceId;
    }

    public String getTokenReferenceId() {
        return tokenReferenceId;
    }

    public String getPanReferenceId() {
        return panReferenceId;
    }

    public void setPanReferenceId(String panReferenceId) {
        this.panReferenceId = panReferenceId;
    }
}
