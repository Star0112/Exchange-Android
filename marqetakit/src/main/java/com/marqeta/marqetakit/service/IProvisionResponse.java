package com.marqeta.marqetakit.service;

/**
 * Created by Jon Weissenburger on 9/28/17.
 */

public interface IProvisionResponse {

    void setTokenizeData(ITokenizeData tokenizeData);
    ITokenizeData getTokenizeData();

}
