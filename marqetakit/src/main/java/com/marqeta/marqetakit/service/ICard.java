package com.marqeta.marqetakit.service;

import java.util.List;

/**
 * Created by Jon Weissenburger on 9/28/17.
 */

public interface ICard {

    void setPanReferenceId(String panReferenceId);
    String getPanReferenceId();

    void setLastFour(String lastFour);
    String getLastFour();

    void setToken(String token);
    String getToken();

    void setState(String state);
    String getState();

    void setNetwork(String network);
    String getNetwork();

    void setExpiration(String expiration);
    String getExpiration();

    void setCanBeProvisioned(boolean canBeProvisioned);
    boolean canBeProvisioned();

    void setDigitalWalletTokens(List<? extends IDigitalWalletToken> digitalWalletTokens);
    List<? extends IDigitalWalletToken> getDigitalWalletTokens();
}
