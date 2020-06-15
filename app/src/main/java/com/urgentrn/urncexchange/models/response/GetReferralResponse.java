package com.urgentrn.urncexchange.models.response;

public class GetReferralResponse extends BaseResponse {

    private ReferralData data;

    public String getCode() {
        return data.code;
    }

    public String getMessage() {
        return data.message;
    }

    private class ReferralData {

        private String code;
        private String message;
    }
}
