package com.urgentrn.urncexchange.models.request;

public class SignupRequest extends UserRequest {

    private String password;
    private boolean overrideValidation = true;
    private String confirmationMethod = "email";
    private String email;
    private String referralCode;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOverrideValidation(boolean overrideValidation) {
        this.overrideValidation = overrideValidation;
    }

    public void setConfirmationMethod(String confirmationMethod) {
        this.confirmationMethod = confirmationMethod;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }
}
