package com.urgentrn.urncexchange.models.response;

public class PhoneConfirmResponse extends BaseResponse {

    private PhoneConfirmData data;

    public String getUsername() {
        return data.username;
    }

    public String getEmail() {
        return data.email;
    }

    private class PhoneConfirmData {

        private String username;
        private String email;
    }
}
