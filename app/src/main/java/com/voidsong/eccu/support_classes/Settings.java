package com.voidsong.eccu.support_classes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Settings {

    public static void save_saved_passwords() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("passwd", saved_passwd);
            jsonObject.put("salt", hash_salt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = jsonObject.toString();

        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret = new SecretKeySpec("SECRET_PHRASE".getBytes("UTF-8"), // TODO different pass phrases for different users
                    "HmacSHA256");
            hmac.init(secret);
            byte[] key = hmac.doFinal(Build.FINGERPRINT.getBytes("UTF-8"));

            AlgorithmParameterSpec ivSpec = new IvParameterSpec("ECCU:SECRET_IV!!".getBytes());  // TODO different IV for different users
            SecretKeySpec newKey = new SecretKeySpec(key, "AES");
            byte[] result = new byte[0];

            try {
                Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
                result = cipher.doFinal(data.getBytes());
            } catch (NoSuchPaddingException | InvalidAlgorithmParameterException |
                    BadPaddingException | IllegalBlockSizeException e) {
                // TODO change - try to hacking us
            }

            FileOutputStream file = context.openFileOutput("saved_keys", context.MODE_PRIVATE);
            file.write(result);
            file.close();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // it can't happen because the HmacSHA256 is exist and
            // UTF-8 is supported by default.
        } catch (InvalidKeyException | IOException e) {

        }
    }

    public static void save(String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("salt", hash_salt);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String data = jsonObject.toString();

        try {
            AlgorithmParameterSpec ivSpec = new IvParameterSpec("ECCU:SECRET_IV!!".getBytes());  // TODO different IV for different users
            SecretKeySpec newKey = new SecretKeySpec(password.getBytes(), "AES");
            byte[] result = new byte[0];

            try {
                Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
                result = cipher.doFinal(data.getBytes());
            } catch (NoSuchPaddingException | InvalidAlgorithmParameterException |
                    BadPaddingException | IllegalBlockSizeException e) {
                // TODO change - try to hacking us
            }

            FileOutputStream file = context.openFileOutput("keys", context.MODE_PRIVATE);
            file.write(result);
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
            // unhandled exception
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

    public static int load(String password) {
        String json_string = "";
        try {
            FileInputStream saved_keys = new FileInputStream("keys");
            json_string = get_all_from_file(saved_keys);
        } catch (FileNotFoundException e) {
            // it can't happen because the previous existence check.
        }

        AlgorithmParameterSpec ivSpec = new IvParameterSpec("ECCU:SECRET_IV!!".getBytes());  // TODO different IV for different users
        SecretKeySpec newKey = new SecretKeySpec(password.getBytes(), "AES");
        byte[] result;
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
            result = cipher.doFinal(json_string.getBytes());
            json_string = result.toString();
        } catch (NoSuchAlgorithmException e) {
            // it can't happen because the HmacSHA256 is exist and
            // UTF-8 is supported by default.
        } catch (NoSuchPaddingException | InvalidAlgorithmParameterException |
                BadPaddingException | IllegalBlockSizeException e) {
            return 2;
            // TODO change - try to hacking us
        } catch(InvalidKeyException e) {
            return 1; // TODO change
        }

        try {
            JSONObject jsonObject = new JSONObject(json_string);
            hash_salt = jsonObject.getString("salt");

            json_string = "";                // change this string for GC.
        } catch (JSONException e) {
            return 3;
        }

        return 0;
    }

    public static int load_saved_passwords() {

        // Read all info from file
        String json_string = "";
        try {
            FileInputStream saved_keys = new FileInputStream("saved_keys");
            json_string = get_all_from_file(saved_keys);
        } catch (FileNotFoundException e) {
            // it can't happen because the previous existence check.
        }

        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret = new SecretKeySpec("SECRET_PHRASE".getBytes("UTF-8"), // TODO different pass phrases for different users
                    "HmacSHA256");
            hmac.init(secret);
            byte[] key = hmac.doFinal(Build.FINGERPRINT.getBytes("UTF-8"));

            AlgorithmParameterSpec ivSpec = new IvParameterSpec("ECCU:SECRET_IV!!".getBytes());  // TODO different IV for different users
            SecretKeySpec newKey = new SecretKeySpec(key, "AES");
            byte[] result;
            try {
                Cipher cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
                result = cipher.doFinal(json_string.getBytes());
                json_string = result.toString();
            } catch (NoSuchPaddingException | InvalidAlgorithmParameterException |
                    BadPaddingException | IllegalBlockSizeException e) {
                // TODO change - try to hacking us
            }

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // it can't happen because the HmacSHA256 is exist and
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
    }

    public static String getIp() {
        if (ip == null) {
            return "192.168.0.106"; // TODO delete
        }
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
