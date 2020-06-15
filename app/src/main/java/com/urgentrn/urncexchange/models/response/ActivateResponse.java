package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.NetworkStatus;

public class ActivateResponse extends BaseResponse {

    private NetworkStatus data;

    public NetworkStatus getData() {
        return data;
    }
}
