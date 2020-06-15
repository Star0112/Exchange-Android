package com.urgentrn.urncexchange.models.response;

public class SignupResponse extends BaseResponse {

    private SignupData data;

    public boolean isSuccess() {
        return data.signupConfirmation;
    }

    private class SignupData {

        private boolean signupConfirmation;
    }
}
