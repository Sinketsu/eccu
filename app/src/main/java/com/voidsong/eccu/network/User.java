package com.voidsong.eccu.network;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import android.util.Base64;

import com.voidsong.eccu.support_classes.Settings;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.support_classes.RequestBodyBuilder;

import org.json.JSONException;
import org.json.JSONObject;

public class User {

    public static final String OK = "OK";
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String CONNECTION_ISSUES = "CONNECTION ISSUES";

    public static void authenticate(String username, String password) {
        status = "";

        MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        String body = new RequestBodyBuilder()
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(API.SCHEME + Settings.getIp() + API.AUTH)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, body))
                .build();

        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    status = CONNECTION_ISSUES;
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.code() == 200) {
                        String text = response.body().string();
                        try {
                            JSONObject jsonObject = new JSONObject(text);
                            String base64_key = jsonObject.getString("key");
                            try {
                                base64_key = new String(Base64.decode(base64_key, Base64.DEFAULT), "UTF-8");
                                cipher_key = base64_key.toCharArray();
                            } catch (UnsupportedEncodingException e) {
                                // TODO change
                            }
                        } catch (JSONException e) {
                            // TODO change
                        }
                        status = OK; // TODO change
                    } else if(response.code() == 401) {
                        status = INVALID_PASSWORD; // TODO change
                    } else {
                        status = CONNECTION_ISSUES;
                    }
                }
            });
        } catch (Exception e) { // TODO change
            e.printStackTrace();
        }
    }


    public static char[] getCipher_key() {
        return cipher_key;
    }

    public static String getStatus() {
        return status;
    }

    private static char[] cipher_key;

    private static OkHttpClient client = Internet.getClient();

    private static String status = "";
}
