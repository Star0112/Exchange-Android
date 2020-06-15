package com.urgentrn.urncexchange.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class StringEncryptUtils {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORM = "AES/CBC/PKCS5Padding";

    private static final Integer ALGORITHM_KEY_SIZE = 16;
    private static final Integer ALGORITHM_IV_SIZE = 16;

    private Context mContext;

    StringEncryptUtils(Context context) {
        mContext = context;
    }

    private byte[] getRandomData(byte[] data) {
        SecureRandom ranGen = new SecureRandom();
        ranGen.nextBytes(data);
        return data;
    }

    private String SHA256(String text) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes());
        byte[] digest = md.digest();

        return Base64.encodeToString(digest, Base64.DEFAULT);
    }

    public static String md5(String text) {
        if (text == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(text.getBytes());
            byte[] digest = md.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) hexString.append(Integer.toHexString(0xFF & b));

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    @SuppressLint("HardwareIds")
    private String getDeviceId() {
        final String deviceId;
        deviceId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (deviceId != null) {
            return deviceId;
        } else {
            return Build.SERIAL;
        }
    }

    private byte[] deriveKey() {
        String androidId = getDeviceId();
        String deviceName = Build.MODEL;
        String manufacturer = Build.MANUFACTURER;

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        String seed = "" + androidId + manufacturer + deviceName + width;

        try {
            byte[] key = new byte[ALGORITHM_KEY_SIZE];
            String hashedKey = SHA256(seed);
            System.arraycopy(hashedKey.getBytes(), 0, key, 0, key.length);
            return key;
        } catch (Exception e) {
            Log.e("ENCRYPTION ERROR", e.getLocalizedMessage());
            return new byte[ALGORITHM_KEY_SIZE];
        }
    }

    private byte[] generateIV() {
        byte[] iv = new byte[ALGORITHM_IV_SIZE];
        return getRandomData(iv);
    }

    String encryptString(String str) {
        if (str == null) return null;
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORM);
            byte[] key = deriveKey();
            SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
            byte[] iv = generateIV();
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encryptedData = cipher.doFinal(str.getBytes());
            byte[] payload = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, payload, 0, iv.length);
            System.arraycopy(encryptedData, 0, payload, iv.length, encryptedData.length);
            return Base64.encodeToString(payload, Base64.NO_WRAP);
        } catch (Exception e) {
            return str;
        }
    }

    String decryptString(String str) {
        if (str == null) return null;
        try {
            byte[] data = Base64.decode(str, Base64.NO_WRAP);
            byte[] iv = Arrays.copyOfRange(data, 0, ALGORITHM_IV_SIZE);
            byte[] encryptedData = Arrays.copyOfRange(data, ALGORITHM_IV_SIZE, data.length);
            Cipher cipher = Cipher.getInstance(TRANSFORM);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(deriveKey(), ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return new String(cipher.doFinal(encryptedData), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return str;
        }
    }
}
