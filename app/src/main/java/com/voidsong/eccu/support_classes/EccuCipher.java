package com.voidsong.eccu.support_classes;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EccuCipher {

    private static Context context;

    public static void setContext(Context _context) {
        context = _context;
    }

    public static String encode64(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    public static byte[] decode64(String data) {
        return Base64.decode(data, Base64.DEFAULT);
    }

    private static SecretKeySpec getSecretKeySpec() throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret = new SecretKeySpec("ECCU".getBytes(), "HmacSHA256");
        hmac.init(secret);
        TelephonyManager manager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = manager.getDeviceId();
        byte[] key = hmac.doFinal(imei.getBytes());
        return new SecretKeySpec(key, "AES");
    }

    private static SecretKeySpec getSecretKeySpec(String password) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret = new SecretKeySpec("ECCU".getBytes(), "HmacSHA256");
        hmac.init(secret);
        byte[] key = hmac.doFinal(password.getBytes());
        return new SecretKeySpec(key, "AES");
    }

    public static byte[] encrypt(String data) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec("ECCU:SECRET_IV!!".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(), ivSpec);
        return cipher.doFinal(data.getBytes());
    }

    public static byte[] encrypt(String data, String password) throws NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec("ECCU:SECRET_IV!!".getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(password), ivSpec);
        return cipher.doFinal(data.getBytes());
    }

    public static String decrypt(byte[] data) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec("ECCU:SECRET_IV!!".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(), ivSpec);
        return new String(cipher.doFinal(data));
    }

    public static String decrypt(byte[] data, String password) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
        AlgorithmParameterSpec ivSpec = new IvParameterSpec("ECCU:SECRET_IV!!".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(password), ivSpec);
        return new String(cipher.doFinal(data));
    }
}
