package com.urgentrn.urncexchange.models.response;

public class PinResponse extends BaseResponse {

    private PinCode data;

    public String getData() {
        return data != null ? data.pin : null;
    }

    private class PinCode {

        private String pin;
    }
}
