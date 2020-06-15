package com.marqeta.marqetakit.service;

/**
 * Created by Jon Weissenburger on 9/28/17.
 */

public interface ITokenizeData {

    void setNetwork(String network);
    String getNetwork();

    void setTokenServiceProvider(String tokenServiceProvider);
    String getTokenServiceProvider();

    void setDisplayName(String displayName);
    String getDisplayName();

    void setLastDigits(String lastDigits);
    String getLastDigits();

    void setOpaquePaymentCard(String opaquePaymentCard);
    String getOpaquePaymentCard();

    void setUserAddress(IUserAddress userAddress);
    IUserAddress getUserAddress();


}
