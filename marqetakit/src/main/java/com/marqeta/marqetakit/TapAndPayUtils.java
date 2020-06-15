package com.marqeta.marqetakit;

import static com.google.android.gms.tapandpay.TapAndPay.CARD_NETWORK_AMEX;
import static com.google.android.gms.tapandpay.TapAndPay.CARD_NETWORK_DISCOVER;
import static com.google.android.gms.tapandpay.TapAndPay.CARD_NETWORK_INTERAC;
import static com.google.android.gms.tapandpay.TapAndPay.CARD_NETWORK_MASTERCARD;
import static com.google.android.gms.tapandpay.TapAndPay.CARD_NETWORK_VISA;
import static com.google.android.gms.tapandpay.TapAndPay.TOKEN_PROVIDER_AMEX;
import static com.google.android.gms.tapandpay.TapAndPay.TOKEN_PROVIDER_DISCOVER;
import static com.google.android.gms.tapandpay.TapAndPay.TOKEN_PROVIDER_EFTPOS;
import static com.google.android.gms.tapandpay.TapAndPay.TOKEN_PROVIDER_GOOGLE;
import static com.google.android.gms.tapandpay.TapAndPay.TOKEN_PROVIDER_INTERAC;
import static com.google.android.gms.tapandpay.TapAndPay.TOKEN_PROVIDER_MASTERCARD;
import static com.google.android.gms.tapandpay.TapAndPay.TOKEN_PROVIDER_OBERTHUR;
import static com.google.android.gms.tapandpay.TapAndPay.TOKEN_PROVIDER_PAYPAL;
import static com.google.android.gms.tapandpay.TapAndPay.TOKEN_PROVIDER_VISA;

/**
 * Created by vincentguerin on 9/13/17.
 */

public class TapAndPayUtils {
    public static int convertTspToEnum(String tokenServiceProvider) {
        if (tokenServiceProvider.equalsIgnoreCase("TOKEN_PROVIDER_GOOGLE")) {
            return TOKEN_PROVIDER_GOOGLE;
        } else if (tokenServiceProvider.equalsIgnoreCase("TOKEN_PROVIDER_AMEX")) {
            return TOKEN_PROVIDER_AMEX;
        } else if (tokenServiceProvider.equalsIgnoreCase("TOKEN_PROVIDER_MASTERCARD")) {
            return TOKEN_PROVIDER_MASTERCARD;
        } else if (tokenServiceProvider.equalsIgnoreCase("TOKEN_PROVIDER_VISA")) {
            return TOKEN_PROVIDER_VISA;
        } else if (tokenServiceProvider.equalsIgnoreCase("TOKEN_PROVIDER_DISCOVER")) {
            return TOKEN_PROVIDER_DISCOVER;
        } else if (tokenServiceProvider.equalsIgnoreCase("TOKEN_PROVIDER_EFTPOS")) {
            return TOKEN_PROVIDER_EFTPOS;
        } else if (tokenServiceProvider.equalsIgnoreCase("TOKEN_PROVIDER_INTERAC")) {
            return TOKEN_PROVIDER_INTERAC;
        } else if (tokenServiceProvider.equalsIgnoreCase("TOKEN_PROVIDER_OBERTHUR")) {
            return TOKEN_PROVIDER_OBERTHUR;
        } else if (tokenServiceProvider.equalsIgnoreCase("TOKEN_PROVIDER_PAYPAL")) {
            return TOKEN_PROVIDER_PAYPAL;
        }

        return 0; // unknown
    }

    public static int convertToConstant(String network) {
        if (network.equalsIgnoreCase("AMEX")) {
            return CARD_NETWORK_AMEX;
        } else if (network.equalsIgnoreCase("DISCOVER")) {
            return CARD_NETWORK_DISCOVER;
        } else if (network.equalsIgnoreCase("INTERAC")) {
            return CARD_NETWORK_INTERAC;
        } else if (network.equalsIgnoreCase("MASTERCARD")) {
            return CARD_NETWORK_MASTERCARD;
        } else if (network.equalsIgnoreCase("VISA")) {
            return CARD_NETWORK_VISA;
        }

        return 1000; // unknown
    }
}
