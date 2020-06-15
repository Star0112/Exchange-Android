package com.marqeta.marqetakit.service;

/**
 * Created by Jon Weissenburger on 9/28/17.
 */

public interface IDigitalWalletToken {

    void setTokenReferenceId(String tokenReferenceId);
    String getTokenReferenceId();

    String getPanReferenceId();
    void setPanReferenceId(String panReferenceId);

}
