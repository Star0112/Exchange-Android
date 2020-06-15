package com.marqeta.marqetakit.service;

/**
 * Created by Jon Weissenburger on 9/28/17.
 */

public interface IProvisionRequest {

    void setCardToken(String cardToken);
    String getCardToken();

    void setDeviceType(String deviceType);
    String getDeviceType();

    void setProvisioningAppVersion(String provisioningAppVersion);
    String getProvisioningAppVersion();

    void setWalletAccountId(String walletAccountId);
    String getWalletAccountId();

    void setDeviceId(String deviceId);
    String getDeviceId();


}
