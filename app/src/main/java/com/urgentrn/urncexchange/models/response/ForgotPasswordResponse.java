package com.urgentrn.urncexchange.models.response;

public class ForgotPasswordResponse extends BaseResponse {

    private CodeData data;

    public String getCodeDeliveryMethod() {
        return data.codeDeliveryMethod;
    }

    public String getCodeDestination() {
        return data.codeDestination;
    }

    private class CodeData {

        private String codeDeliveryMethod;
        private String codeDestination;
    }
}
