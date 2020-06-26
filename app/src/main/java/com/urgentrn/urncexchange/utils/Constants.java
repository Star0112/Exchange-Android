package com.urgentrn.urncexchange.utils;

public class Constants {

    public static String API_URL = "http://192.168.1.120:3333";
    public static String SOCKET_URI = "wss://urnptesten.urnbe.com/";
    public static final int CONNECT_TIMEOUT = 90;

    public static final int API_REQUEST_INTERVAL_DEFAULT = 30 * 1000;
    public static final int API_REQUEST_INTERVAL_SHORT = 10 * 1000;
    public static final int SOCKET_TIMEOUT = 5 * 1000;
    static final int MINIMUM_LENGTH_PASSWORD = 8;
    static final int MINIMUM_LENGTH_USERNAME = 3;
    public static final int PASSCODE_LENGTH = 4;
    public static final boolean USE_BIOMETRICS = true;
    public static final boolean USE_ONBOARDING = true;
    public static final boolean USE_STRIPE_TOKEN = true;

    public static String getApiUrl(String domain) {
        return "https://" + domain + "/api/";
    }


    public enum SecurityType {
        DEFAULT,
        TRANSACTION,
        CARD,
        SETTING,
    }

    public enum Action {
        HOME
    }
}
