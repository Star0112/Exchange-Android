package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.card.token.PushTokenizeRequestData;

public class TokenResponse extends BaseResponse {

    private String created_time;
    private String last_modified_time;
    private PushTokenizeRequestData push_tokenize_request_data;

    public String getCreatedTime() {
        return created_time;
    }

    public String getLastModifiedTime() {
        return last_modified_time;
    }

    public PushTokenizeRequestData getPushTokenizeRequestData() {
        return push_tokenize_request_data;
    }
}
