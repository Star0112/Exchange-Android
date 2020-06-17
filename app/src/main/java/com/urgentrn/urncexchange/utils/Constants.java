package com.urgentrn.urncexchange.utils;

import com.urgentrn.urncexchange.BuildConfig;

public class Constants {

    public static String API_URL = "https://svrp1prod.walletsdk.com";
    public static String COUNTRY_NAME = "";
    public static final String API_VERSION = "1.0.0";
    public static final String ENVIRONMENT = BuildConfig.ENVIRONMENT;
    public static final int CONNECT_TIMEOUT = 90;

    public static final int API_REQUEST_INTERVAL_DEFAULT = 30 * 1000;
    public static final int API_REQUEST_INTERVAL_SHORT = 10 * 1000;
    public static final boolean DEFAULT_BACK_BEHAVIOR = true;
    public static final int DEFAULT_LIST_LIMIT_SMALL = 6;
    public static final int DEFAULT_PAGE_LIMIT = 10;
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    public static final boolean ENABLE_US_SIGN_UP = false;
    static final int MINIMUM_LENGTH_PASSWORD = 8;
    public static final int MINIMUM_LENGTH_SSN = 9;
    static final int MINIMUM_LENGTH_USERNAME = 3;
    public static final int PHONE_VERIFICATION_CODE_LENGTH = 6;
    public static final int PASSCODE_LENGTH = 4;
    public static final int PIN_LENGTH = 4;
    public static final boolean USE_API_FROM_CONFIG = false;
    public static final boolean USE_BIOMETRICS = true;
    public static final boolean USE_ONBOARDING = true;
    public static final boolean USE_COMBINED_LOGIN = true;
    public static final boolean USE_COMBINED_SIGNUP = true;
    public static final boolean USE_FAVORITES = true;
    public static final boolean USE_GOOGLE_PAY = false;
    public static final boolean USE_NESTED_LIST_HEADER = false;
    public static final boolean USE_PORTFOLIO_BALANCE = false;
    public static final boolean USE_RE_ENTRY = !BuildConfig.DEBUG;
    public static final boolean USE_STRIPE_TOKEN = true;
    public static final boolean USE_SYMBOL_DATA = false;

    public static String getApiUrl(String domain) {
        return "https://" + domain + "/api/";
    }

    public enum VerifyType {
        EMAIL,
        PHONE,
        PASSWORD,
        TIER1,
        TIER2,
        BANK,
        CARD_ORDER,
        CARD_ACTIVATE,
        CARD_CREATE_PIN,
        CARD_UPDATE_PIN,
        CARD_VIEW_PIN,
        CARD_ORDER_PHYSICAL,
        CARD_UPGRADE,
    }

    public enum NinType {
        PASSPORT,
        DRIVER_LICENSE,
        SSN,
        NIN,
    }

    public enum SecurityType {
        DEFAULT,
        TRANSACTION,
        CARD,
        SETTING,
    }

    public enum SendType {
        USERNAME,
        EMAIL,
        MOBILE,
        ADDRESS
    }

    /**
     * fragment transaction
     */
    public enum Action {
        HOME
    }

    public static class ActivityRequestCodes {

        public static final int AMOUNT_INPUT_FROM = 101;
        public static final int AMOUNT_INPUT_TO = 102;
        public static final int CONTACT = 111;
        public static final int WALLET_ADDRESS = 112;
        public static final int SCAN_CODE = 121;
        public static final int SELECT_ASSETS = 113;
        public static final int SELECT_PICTURE = 114;
    }

    public static class PermissionRequestCodes {

        public static final int CAMERA_PERMISSION = 201;
        public static final int STORAGE_READ_ACCESS_PERMISSION = 202;
    }
}
