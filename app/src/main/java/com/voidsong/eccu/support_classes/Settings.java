package com.voidsong.eccu.support_classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.annotation.NonNull;

public class Settings {

    public static int save() {
        return 0;
    }

    public static int load(boolean has_saved_keys) {
        if (context == null) {
            return 1;
        }

        // Read all info from file
        String json_string = "";
        try {
            FileInputStream info = context.openFileInput("info");
            json_string = get_all_from_file(info);
        } catch (FileNotFoundException e) {
            // it can't happen because the previous existence check.
        }

        if (json_string.isEmpty()) {
            return 2;
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

        if (has_saved_keys) {
            try {
                FileInputStream saved_keys = new FileInputStream("saved_keys");
                json_string = get_all_from_file(saved_keys);
            } catch (FileNotFoundException e) {
                // it can't happen because the previous existence check.
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
