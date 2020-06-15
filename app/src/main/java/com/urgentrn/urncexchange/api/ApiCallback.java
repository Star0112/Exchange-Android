package com.urgentrn.urncexchange.api;

import com.urgentrn.urncexchange.models.response.BaseResponse;

public interface ApiCallback {

    void onResponse(BaseResponse response);

    void onFailure(String message);

}
