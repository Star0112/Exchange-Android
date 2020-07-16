package com.urgentrn.urncexchange.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.urgentrn.urncexchange.BuildConfig;
import com.urgentrn.urncexchange.ExchangeApplication;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        Pattern pattern = true ? Patterns.EMAIL_ADDRESS : Pattern.compile("^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * method is used for checking valid password strength
     *
     * @param password
     * @return boolean true for valid, false for invalid
     */
    public static boolean isPasswordValid(String password) {
        return password.length() >= Constants.MINIMUM_LENGTH_PASSWORD;
    }

    public static boolean isUsernameInvalid(String username) {
        return username.length() < Constants.MINIMUM_LENGTH_USERNAME;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    public static void hideKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null || view == null) return;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Date stringToDate(String dateString, String format) {
        return stringToDate(dateString, format, null);
    }

    public static Date stringToDate(String dateString, String format, String timezone) {
        if (dateString != null && !TextUtils.isEmpty(dateString)) {
            try {
                final SimpleDateFormat sdf = new SimpleDateFormat(format != null ? format : "yyyy-MM-dd hh:mm:ss", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone(timezone == null ? "America/New_York" : timezone));
                return sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new Date();
    }

    public static String dateToString(Date date, String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format != null ? format : "hh:mm a EEEE, MMM dd, yyyy", Locale.US);
        return sdf.format(date);
    }

    public static String dateToString(Calendar calendar, String format) {
        final SimpleDateFormat sdf = new SimpleDateFormat(format != null ? format : "yyyy-MM-dd", Locale.US);
        return sdf.format(calendar.getTime());
    }

    public static int daysFromNow(String dateString) {
        return (int)((System.currentTimeMillis() - stringToDate(dateString, null).getTime()) / (1000 * 60 * 60 * 24));
    }

    public static String formattedDateTime(String dateTime) {
        return dateToString(stringToDate(dateTime, null), "dd/mm/yyyy hh:mm");
    }

    public static String formattedNumber(int number) {
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        return formatter.format(number);
    }

    public static String formattedNumber(double number) {
        return formattedNumber(number, 2, 8);
    }

    public static String formattedNumber(double number, int minFractionDigits, int maxFractionDigits) {
        final DecimalFormat formatter = new DecimalFormat("#,###.#####");
        formatter.setMinimumFractionDigits(minFractionDigits);
        formatter.setMaximumFractionDigits(maxFractionDigits);
        return formatter.format(number);
    }

    public static String formattedNumber(String s) {
        try {
            return formattedNumber(Double.parseDouble(s));
        } catch (NumberFormatException e) {
            return s == null ? null : !s.contains(".") ? s : s.replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }

    public static String formatLargeNumber(double value) {
        String suffix = "";
        if (value >= Math.pow(10, 9)) {
            value = value / Math.pow(10, 9);
            suffix = " Billion";
        } else if (value >= Math.pow(10, 6)) {
            value = value / Math.pow(10, 6);
            suffix = " Million";
        }
        return formattedNumber(value) + suffix;
    }

    public static String maskedCardNumber(String cardNumber) {
        if (cardNumber == null) return "";
        String maskedNumber = "";
        if (cardNumber.length() > 4) {
            for (int i = 0; i < cardNumber.length() - 4; i ++) {
                maskedNumber += "•";
            }
            maskedNumber += cardNumber.substring(cardNumber.length() - 4);
        } else {
            maskedNumber = cardNumber;
        }
        return addSeparator(maskedNumber, " ", 4);
    }

    public static String maskedPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) return "";
        final StringBuilder str = new StringBuilder(phoneNumber);
        int idx = str.length();
        int lastIdx = 0;
        while (--idx > 0) {
            if (str.charAt(idx) == '-' || str.charAt(idx) == ' ') {
                str.replace(idx, idx + 1, " ");
            } else {
                if (++lastIdx > 4) {
                    str.replace(idx, idx + 1, "X");
                }
            }
        }
        return str.toString();
    }

    public static String addSeparator(String oldString, String separator, int every) {
        final StringBuilder str = new StringBuilder(oldString);
        int idx = str.length() - every;
        while (idx > 0) {
            str.insert(idx, separator != null ? separator : " ");
            idx = idx - every;
        }
        return str.toString();
    }

    public static String getFirstLetters(String name) {
        if (name == null) return null;
        final StringBuilder builder = new StringBuilder();
        for (String word : name.split(" ", 2)) {
            builder.append(word.charAt(0));
        }
        return builder.toString();
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getDeviceCountryCode(Context context) {
        String countryCode;
        final TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            countryCode = tm.getSimCountryIso();
            if (!TextUtils.isEmpty(countryCode)) {
                return countryCode;
            }
            countryCode = tm.getNetworkCountryIso();
            if (!TextUtils.isEmpty(countryCode)) {
                return countryCode;
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            countryCode = context.getResources().getConfiguration().getLocales().get(0).getCountry();
        } else {
            countryCode = context.getResources().getConfiguration().locale.getCountry();
        }
        return countryCode;
    }

    public static void transferActivity(Activity activity, Class<?> cls) {
        final Intent intent = new Intent(activity, cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }

    public static String getDeviceInfo() {
        if (ExchangeApplication.getApp().getUser() == null) return "";
        return ExchangeApplication.getApp().getUser().getUsername()
                + " - " + "android"
                + " - " + BuildConfig.VERSION_NAME
                + " - " + BuildConfig.VERSION_CODE
//                + " - " + Build.DEVICE
                + " - " + Build.MODEL
//                + " - " + Build.PRODUCT
                + " - " + "Android " + Build.VERSION.RELEASE;
    }

    public static void sendMail(Activity activity) {
        final Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("@urgentrn.com"));
        intent.putExtra(Intent.EXTRA_TEXT, getDeviceInfo());
        if (activity != null) {
            activity.startActivity(Intent.createChooser(intent, "Send mail"));
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ExchangeApplication.getApp().startActivity(Intent.createChooser(intent, "Send mail"));
        }
    }

    public static boolean isFromSplash(Intent intent) {
        return intent.getBooleanExtra("from_splash", false);
    }

    public static String addChar(String str, char ch, int position) {
        int len = str.length();
        char[] updatedArr = new char[len + 1];
        str.getChars(0, position, updatedArr, 0);
        updatedArr[position] = ch;
        str.getChars(position, len, updatedArr, position + 1);
        return new String(updatedArr);
    }
}
