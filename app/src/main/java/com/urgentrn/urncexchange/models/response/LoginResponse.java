package com.urgentrn.urncexchange.models.response;

import com.urgentrn.urncexchange.models.TwoFactorData;
import com.urgentrn.urncexchange.models.User;

public class LoginResponse extends BaseResponse {

    private LoginData data;

    public boolean hasTwoFactorAuth() {
        return data.twoFactorAuth;
    }

    public TwoFactorData getTwoFactorData() {
        return data.twoFactorData;
    }

    public String getAccessToken() {
        return data.accessToken;
    }

    public String getRefreshToken() {
        return data.refreshToken;
    }

    public User getUser() {
        return data.user;
    }

    public String getSession() {
        return data.session;
    }

    private class LoginData {

        private boolean twoFactorAuth;
        private TwoFactorData twoFactorData;
        private String accessToken;
        private String refreshToken;
        private User user;
        private String session;
    }
}