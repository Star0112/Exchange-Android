package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.AppConfig;

public class GetVersionsResponse extends BaseResponse {

    private AppConfig data;

    public AppConfig getData() {
        return data;
    }
}
