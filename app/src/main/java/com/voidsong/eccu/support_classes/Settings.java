package com.voidsong.eccu.support_classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Base64;

import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

public class Settings {

    public static int save() {
        return 0;
    }

    public static void loadInfo() {

        // Read all info from file
        String json_string = "";
        try {
            FileInputStream info = context.openFileInput("info");
            json_string = get_all_from_file(info);
        } catch (FileNotFoundException e) {
            // it can't happen because the previous existence check.
        }

        if (json_string.isEmpty()) {
            login = "";
            ip = "";
            return;
        }

        // Fill our fields from JSON string
        try {
            JSONObject jsonObject = new JSONObject(json_string);
            login = jsonObject.getString("login");
            ip = jsonObject.getString("ip");

            json_string = "";                // change this string for GC.
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static int load(boolean has_saved_keys) {

        // Read all info from file
        String json_string = "";
        if (has_saved_keys) {
            try {
                FileInputStream saved_keys = new FileInputStream("saved_keys");
                json_string = get_all_from_file(saved_keys);
            } catch (FileNotFoundException e) {
                // it can't happen because the previous existence check.
            }

            // TODO decrypt a string
            try {
                Mac hmac = Mac.getInstance("HmacSHA384");
                SecretKeySpec secret = new SecretKeySpec("SECRET_PHRASE".getBytes("UTF-8"),
                        "HmacSHA384");
                hmac.init(secret);
                byte[] hash = hmac.doFinal(Build.FINGERPRINT.getBytes("UTF-8"));
                char[] key = Base64.encodeToString(hash, Base64.DEFAULT).toCharArray();


            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                // it can't happen because the HmacSHA384 is exist and
                // UTF-8 is supported by default.
            } catch (InvalidKeyException e) {
                e.printStackTrace(); // TODO change
            }

            try {
                JSONObject jsonObject = new JSONObject(json_string);
                saved_passwd = jsonObject.getString("passwd");
                hash_salt = jsonObject.getString("salt");

                json_string = "";                // change this string for GC.
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return 0;
        } else {
            return 0; // TODO change!!!!
        }
    }

    public static String getIp() {
        if (ip == null) {
            return "192.168.0.106"; // TODO delete
        }
        return ip;
    }

    public static void setContext(Context context) {
        Settings.context = context;
    }

    public static String getLogin() {
        return login;
    }

    private static Context context;

    private static String login;
    private static String ip;
    private static String hash_salt;
    private static String saved_passwd;

    @NonNull
    private static String get_all_from_file(FileInputStream fileInputStream) {
        String result = "";
        int c;
        try {
            while ( (c = fileInputStream.read()) != -1) {
                result += (char)c;
            }
            fileInputStream.close();
            return result;
        } catch (IOException e) {
            return "";
        }
    }
}
