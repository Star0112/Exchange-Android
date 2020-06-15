package com.urgentrn.urncexchange.models.card.token;

import java.util.List;

public class MarqetaCard {

    private String panReferenceId;
    private String lastFour;
    private String token;
    private String state;
    private String network;
    private String expiration;
    private boolean canBeProvisioned = true;
    private List digitalWalletTokens;

    public void setPanReferenceId(String panReferenceId) {
        this.panReferenceId = panReferenceId;
    }

    public String getPanReferenceId() {
        return panReferenceId;
    }

    public void setLastFour(String lastFour) {
        this.lastFour = lastFour;
    }

    public String getLastFour() {
        return lastFour;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getNetwork() {
        return network;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setCanBeProvisioned(boolean canBeProvisioned) {
        this.canBeProvisioned = canBeProvisioned;
    }

    public boolean canBeProvisioned() {
        return canBeProvisioned;
    }

    public void setDigitalWalletTokens(List digitalWalletTokens) {
        this.digitalWalletTokens = digitalWalletTokens;
    }

    public List getDigitalWalletTokens() {
        return digitalWalletTokens;
    }
}
