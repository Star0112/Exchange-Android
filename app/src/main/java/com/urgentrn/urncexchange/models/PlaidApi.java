package com.urgentrn.urncexchange.models;

import com.urgentrn.urncexchange.models.response.BaseResponse;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlaidApi extends BaseResponse implements Serializable {

    private boolean selectAccount;
    private String env;
    private String apiVersion;
    private String version;
    private String clientName;
    private String key;
    private List<String> product;
    private List<String> countryCodes;
    private String callbackPath;

    public boolean isSelectAccount() {
        return selectAccount;
    }

    public String getEnv() {
        return env;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getVersion() {
        return version;
    }

    public String getClientName() {
        return clientName;
    }

    public String getKey() {
        return key;
    }

    public List<String> getProduct() {
        return product;
    }

    public List<String> getCountryCodes() {
        return countryCodes != null ? countryCodes : new ArrayList<>();
    }

    public String getCallbackPath() {
        return callbackPath;
    }
}
