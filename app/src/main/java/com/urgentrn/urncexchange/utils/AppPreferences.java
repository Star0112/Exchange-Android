package com.urgentrn.urncexchange.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.urgentrn.urncexchange.models.Transaction;
import com.urgentrn.urncexchange.models.User;

import java.util.List;

public class AppPreferences {

    public static final String PREFERENCE_NAME = "EXCHANGE_PREFERENCE";
    private static final String PREFERENCE_REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String PREFERENCE_ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String PREFERENCE_PUSH_TOKEN = "PUSH_TOKEN";
    private static final String PREFERENCE_EMAIL = "EMAIL";
    private static final String PREFERENCE_USERNAME = "USERNAME";
    private static final String PREFERENCE_FIRSTNAME = "FIRSTNAME";
    private static final String PREFERENCE_LASTNAME = "USERLASTNAME";
    private static final String PREFERENCE_PHONE = "USERPHONE";
    private static final String PREFERENCE_COUNTRY = "COUNTRY";
    private static final String PREFERENCE_PASSWORD = "PASSWORD";
    private static final String PREFERENCE_PASSCODE = "PIN_CODE";
    private static final String PREFERENCE_FINGERPRINT_ENABLED = "FINGERPRINT_ENABLED";
    private static final String PREFERENCE_PASSCODE_ENABLED = "PASSCODE_ENABLED";

    private final SharedPreferences preferences;
    private Context context;

    public AppPreferences(Context context, String preferenceName) {
        this.preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        this.context = context;
    }

    public void clear() {
        preferences.edit().clear().apply();
    }

    public String getRefreshToken() {
        return preferences.getString(PREFERENCE_REFRESH_TOKEN, null);
    }

    public void setRefreshToken(String token) {
        preferences.edit().putString(PREFERENCE_REFRESH_TOKEN, token).apply();
    }

    public String getToken() {
        return new StringEncryptUtils(context).decryptString(preferences.getString(PREFERENCE_ACCESS_TOKEN, null));
    }

    public void setToken(String token) {
        preferences.edit().putString(PREFERENCE_ACCESS_TOKEN, new StringEncryptUtils(context).encryptString(token)).apply();
    }

    public String getPushToken() {
        return preferences.getString(PREFERENCE_PUSH_TOKEN, null);
    }

    public void setPushToken(String token) {
        preferences.edit().putString(PREFERENCE_PUSH_TOKEN, token).apply();
    }

    public String getEmail() {
        return new StringEncryptUtils(context).decryptString(preferences.getString(PREFERENCE_EMAIL, null));
    }

    public void setEmail(String email) {
        preferences.edit().putString(PREFERENCE_EMAIL, new StringEncryptUtils(context).encryptString(email)).apply();
    }

    public String getUsername() {
        return new StringEncryptUtils(context).decryptString(preferences.getString(PREFERENCE_USERNAME, null));
    }

    public void setUsername(String username) {
        preferences.edit().putString(PREFERENCE_USERNAME, new StringEncryptUtils(context).encryptString(username)).apply();
    }

    public String getFirstname() {
        return new StringEncryptUtils(context).decryptString(preferences.getString(PREFERENCE_FIRSTNAME, null));
    }

    public void setFirstname(String firstname) {
        preferences.edit().putString(PREFERENCE_FIRSTNAME, new StringEncryptUtils(context).encryptString(firstname)).apply();
    }

    public String getLastname() {
        return new StringEncryptUtils(context).decryptString(preferences.getString(PREFERENCE_LASTNAME, null));
    }

    public void setLastname(String lastname) {
        preferences.edit().putString(PREFERENCE_LASTNAME, new StringEncryptUtils(context).encryptString(lastname)).apply();
    }

    public String getPhone() {
        return new StringEncryptUtils(context).decryptString(preferences.getString(PREFERENCE_PHONE, null));
    }

    public void setPhone(String phone) {
        preferences.edit().putString(PREFERENCE_PHONE, new StringEncryptUtils(context).encryptString(phone)).apply();
    }

    public String getCountry() {
        return new StringEncryptUtils(context).decryptString(preferences.getString(PREFERENCE_COUNTRY, null));
    }

    public void setCountry(String country) {
        preferences.edit().putString(PREFERENCE_COUNTRY, new StringEncryptUtils(context).encryptString(country)).apply();
    }

    public String getPassword() {
        return new StringEncryptUtils(context).decryptString(preferences.getString(PREFERENCE_PASSWORD, null));
    }

    public void setPassword(String password) {
        preferences.edit().putString(PREFERENCE_PASSWORD, new StringEncryptUtils(context).encryptString(password)).apply();
    }

    public String getPasscode() {
        return new StringEncryptUtils(context).decryptString(preferences.getString(PREFERENCE_PASSCODE, null));
    }

    public void setPasscode(String passcode) {
        preferences.edit().putString(PREFERENCE_PASSCODE, new StringEncryptUtils(context).encryptString(passcode)).apply();
    }

    public boolean isFingerprintEnabled() {
        return preferences.getBoolean(PREFERENCE_FINGERPRINT_ENABLED, false);
    }

    public void setFingerprintEnabled(boolean isFingerprintEnabled) {
        preferences.edit().putBoolean(PREFERENCE_FINGERPRINT_ENABLED, isFingerprintEnabled).apply();
    }

    public boolean isPasscodeEnabled() {
        return preferences.getBoolean(PREFERENCE_PASSCODE_ENABLED, false);
    }

    public void setPasscodeEnabled(boolean isPasscodeEnabled) {
        preferences.edit().putBoolean(PREFERENCE_PASSCODE_ENABLED, isPasscodeEnabled).apply();
    }
}
