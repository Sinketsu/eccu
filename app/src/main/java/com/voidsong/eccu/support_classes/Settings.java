package com.voidsong.eccu.support_classes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Settings {

    public static final String DEFAULT_HASH = "ECCU";
    public static final String DEFAULT_IV = "ECCU:SECRET_IV!!";
    public static final String NO_SALT = "NO_SALT";
    public static final String DEFAULT_IP = "127.0.0.1";

    public static void save_saved_passwords() {
        context.deleteFile("keys");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("passwd", saved_passwd);
            jsonObject.put("salt", hash_salt);
        } catch (JSONException e) {
        }
        String data = jsonObject.toString();
        try {
            byte[] result = new byte[0];
            try {
                result = EccuCipher.encrypt(data);
            } catch (NoSuchPaddingException | InvalidAlgorithmParameterException |
                    BadPaddingException | IllegalBlockSizeException e) {
                // TODO change - try to hacking us
            }

            String encoded_data = EccuCipher.encode64(result);
            FileOutputStream file = context.openFileOutput("saved_keys", Context.MODE_PRIVATE);
            file.write(encoded_data.getBytes());
            file.close();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // it can't happen because the HmacSHA256 is exist and
            // UTF-8 is supported by default.
        } catch (InvalidKeyException | IOException e) {

        }
    }

    public static void save(String password) {
        context.deleteFile("saved_keys");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("salt", hash_salt);
        } catch (JSONException e) {
        }
        String data = jsonObject.toString();

        try {
            byte[] result = new byte[0];

            try {
                result = EccuCipher.encrypt(data, password);
            } catch (NoSuchPaddingException | InvalidAlgorithmParameterException |
                    BadPaddingException | IllegalBlockSizeException e) {
                // TODO change - try to hacking us
            }

            String encoded_data = EccuCipher.encode64(result);

            FileOutputStream file = context.openFileOutput("keys", context.MODE_PRIVATE);
            file.write(encoded_data.getBytes());
            file.close();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // it can't happen because the HmacSHA256 is exist and
            // UTF-8 is supported by default.
        } catch (InvalidKeyException | IOException e) {

        }
    }

    public static void saveInfo(String user_login) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ip", ip);
            jsonObject.put("login", user_login);
            jsonObject.put("state", state);
            jsonObject.put("IV", new String(IV, "UTF-8"));
            jsonObject.put("has_hash", HasHash);
            String data = jsonObject.toString();

            FileOutputStream info = context.openFileOutput("info", context.MODE_PRIVATE);
            info.write(data.getBytes());
            info.close();
        } catch (JSONException | IOException e) {
            // Ignore
            // TODO change
        }
    }


    public static void loadInfo() {

        // Read all info from file
        String json_string = "";
        try {
            FileInputStream info = context.openFileInput("info");
            json_string = get_all_from_file(info);
        } catch (FileNotFoundException e) {
            json_string = "";
        }

        if (json_string.isEmpty()) {
            login = "";
            ip = Settings.DEFAULT_IP;
            state = false;
            return;
        }

        // Fill our fields from JSON string
        try {
            JSONObject jsonObject = new JSONObject(json_string);
            login = jsonObject.getString("login");
            ip = jsonObject.getString("ip");
            state = jsonObject.getBoolean("state");
            IV = jsonObject.getString("IV").getBytes();
            HasHash = jsonObject.getBoolean("has_hash");

            json_string = "";                // change this string for GC.
        } catch (JSONException e) {
            login = "";
            ip = Settings.DEFAULT_IP;
            state = false;
        }
    }

    public static int load(String password) {
        String string = "";
        try {
            FileInputStream saved_keys = new FileInputStream("keys");
            string = get_all_from_file(saved_keys);
            saved_keys.close();
        } catch (IOException e) {
            // it can't happen because the previous existence check.
        }

        byte[] data = EccuCipher.decode64(string);

        String result = "";
        try {
            result = EccuCipher.decrypt(data, password);
        } catch (NoSuchAlgorithmException e) {
            // it can't happen because the HmacSHA256 is exist and
            // UTF-8 is supported by default.
        } catch (NoSuchPaddingException | InvalidAlgorithmParameterException |
                BadPaddingException | IllegalBlockSizeException e) {
            // TODO change - try to hacking us
        } catch(InvalidKeyException e) {
            // TODO change
        }

        try {
            JSONObject jsonObject = new JSONObject(result);
            hash_salt = jsonObject.getString("salt");

            result = "";                // change this string for GC.
        } catch (JSONException e) {
        }

        return 0;
    }

    public static int load_saved_passwords() {

        // Read all info from file
        String string = "";
        try {
            FileInputStream saved_keys = context.openFileInput("saved_keys");
            string = get_all_from_file(saved_keys);
            saved_keys.close();
        } catch (IOException e) {
            e.printStackTrace();
            // it can't happen because the previous existence check.
        }
        byte[] data = EccuCipher.decode64(string);
        String result = "";

        try {
            result = EccuCipher.decrypt(data);
        } catch (NoSuchPaddingException | InvalidAlgorithmParameterException |
                BadPaddingException | IllegalBlockSizeException e) {
            // TODO change - try to hacking us
        } catch (NoSuchAlgorithmException e) {
            // it can't happen because the HmacSHA256 is exist and
            // UTF-8 is supported by default.
        } catch (InvalidKeyException e) {
            // TODO change
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            saved_passwd = jsonObject.getString("passwd");
            hash_salt = jsonObject.getString("salt");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String getIp() {
        if (ip == null)
            return DEFAULT_IP;
        return ip;
    }

    public static void setIp(String IP) {
        ip = IP;
    }

    public static void setContext(Context context) {
        Settings.context = context;
    }

    public static String getLogin() {
        return login;
    }

    public static String getSaved_passwd() {
        return saved_passwd;
    }

    public static void setSaved_passwd(String passwd) {
        saved_passwd = passwd;
    }

    public static void setState(boolean _state) {
        state = _state;
    }

    public static boolean getState() {
        return state;
    }

    public static String getHash_salt() {
        return hash_salt;
    }

    public static void setHash_salt(String salt) {
        hash_salt = salt;
    }

    public static byte[] getIV() {
        return IV;
    }

    public static void setIV(byte[] iv) {
        IV = iv;
    }

    public static void setHasHash(boolean state) {
        HasHash = state;
    }

    public static boolean ifHasHash() {
        return HasHash;
    }

    private static Context context;

    private static boolean state;
    private static String login;
    private static String ip;
    private static String hash_salt = NO_SALT;
    private static String saved_passwd;
    private static byte[] IV;
    private static boolean HasHash = false;

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
