package com.urgentrn.urncexchange.utils;

import com.urgentrn.urncexchange.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CardUtils {

    public static int getBrandImage(String brand) {
        if (brand == null) return R.mipmap.ic_new_bank;
        if (brand.equalsIgnoreCase("visa")) return R.mipmap.card_visa;
        else if (brand.equalsIgnoreCase("mastercard")) return R.mipmap.card_mastercard;
        else if (brand.equalsIgnoreCase("discover")) return R.mipmap.card_discover;
        else if (brand.equalsIgnoreCase("amex")) return R.mipmap.card_amex;
        else if (brand.equalsIgnoreCase("maestro")) return R.mipmap.card_maestro;
        else if (brand.equalsIgnoreCase("jcb")) return R.mipmap.card_jcb;
        else if (brand.equalsIgnoreCase("apple")) return R.mipmap.card_apple;
        else if (brand.equalsIgnoreCase("google")) return R.mipmap.google_pay2;
        return R.mipmap.ic_new_bank;
    }

    public static String getBrandName(String brand) {
        if (brand == null) return "";
        if (brand.equalsIgnoreCase("visa")) return "Visa Card";
        else if (brand.equalsIgnoreCase("mastercard")) return "Mastercard";
        else if (brand.equalsIgnoreCase("discover")) return "Discover Card";
        else if (brand.equalsIgnoreCase("amex")) return "American Express";
        else if (brand.equalsIgnoreCase("maestro")) return "Maestro";
        else if (brand.equalsIgnoreCase("jcb")) return "JCB";
        else if (brand.equalsIgnoreCase("apple")) return "Apple Pay";
        else if (brand.equalsIgnoreCase("google")) return "Google Pay";
        return brand;
    }

    public static String getBrand(String number) {
        if (isVisa(number)) return "Visa Card";
        if (isMasterCard(number)) return "Mastercard";
        if (isDiscover(number)) return "Discover Card";
        if (isAmex(number)) return "American Express";
        return "";
    }

    public static boolean isValid(String number) {
        return isVisa(number) || isMasterCard(number) || isDiscover(number) || isAmex(number);
    }

    private static boolean isValid(String number, String regex) {
        if (number == null) return false;
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public static boolean isVisa(String number) {
        return isValid(number, "^4[0-9]{12}(?:[0-9]{3})?$");
    }

    public static boolean isMasterCard(String number) {
        return isValid(number, "^5[1-5][0-9]{14}$");
    }

    public static boolean isDiscover(String number) {
        return isValid(number, "^6(?:011|5[0-9]{2})[0-9]{12}$");
    }

    public static boolean isAmex(String number) {
        return isValid(number, "^3[47][0-9]{13}$");
    }
}
