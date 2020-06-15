package com.urgentrn.urncexchange.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.urgentrn.urncexchange.models.Transaction;

import java.util.List;

public class AppPreferences {

    public static final String PREFERENCE_NAME = "EXCHANGE_PREFERENCE";
    private static final String PREFERENCE_REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String PREFERENCE_ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String PREFERENCE_PUSH_TOKEN = "PUSH_TOKEN";
    private static final String PREFERENCE_EMAIL = "EMAIL";
    private static final String PREFERENCE_USERNAME = "USERNAME";
    private static final String PREFERENCE_PASSWORD = "PASSWORD";
    private static final String PREFERENCE_PASSCODE = "PIN_CODE";
    private static final String PREFERENCE_FINGERPRINT_ENABLED = "FINGERPRINT_ENABLED";
    private static final String PREFERENCE_PASSCODE_ENABLED = "PASSCODE_ENABLED";
    private static final String PREFERENCE_TERMS = "TERMS_ACCEPTED";
    private static final String PREFERENCE_TRANSACTION_LIMIT = "TRANSACTION_LIMIT";
    private static final String PREFERENCE_FAVORITE_SYMBOLS = "FAVORITE_SYMBOLS";

    private final SharedPreferences preferences;
    private Context context;

    public AppPreferences(Context context, String preferenceName) {
        this.preferences = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
        this.context = context;
    }

    public void clear() {
        final String username = getUsername();
        preferences.edit().clear().apply();
        setUsername(username);
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

    public boolean termsAccepted() {
        return preferences.getBoolean(PREFERENCE_TERMS, false);
    }

    public void acceptTerms(boolean isAccepted) {
        preferences.edit().putBoolean(PREFERENCE_TERMS, isAccepted).apply();
    }

    public void setTransactionLimit(Transaction transaction) {
        final String json = new Gson().toJson(transaction);
        preferences.edit().putString(PREFERENCE_TRANSACTION_LIMIT, json).apply();
    }

    public Transaction getTransactionLimit() {
        final String json = preferences.getString(PREFERENCE_TRANSACTION_LIMIT, null);
        return json == null ? null : new Gson().fromJson(json, Transaction.class);
    }

    public void setFavoriteSymbols(List<String> symbols) {
        final String json = new Gson().toJson(symbols);
        preferences.edit().putString(PREFERENCE_FAVORITE_SYMBOLS, json).apply();
    }

    public List<String> getFavoriteSymbols() {
        final String json = preferences.getString(PREFERENCE_FAVORITE_SYMBOLS, null);
        return json == null ? null : new Gson().fromJson(json, new TypeToken<List<String>>(){}.getType());
    }
}
