package com.urgentrn.urncexchange.models.response;

public class MembershipResponse extends BaseResponse {
    private message data;

    public String getMessage() {
        return this.data.message;
    }

    public class message {
        private String message;
    }
}
