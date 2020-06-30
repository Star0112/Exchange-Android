package com.urgentrn.urncexchange.models.response;

public class PurchaseStatusResponse extends BaseResponse {
    private status errors;

    public String getData() { return errors.message; }

    private class status {
        private String code;
        private String message;
    }
}
