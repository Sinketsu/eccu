package com.voidsong.eccu.network;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import com.voidsong.eccu.fragments.FragmentCamera;
import com.voidsong.eccu.fragments.FragmentWeather;
import com.voidsong.eccu.exceptions.SecurityErrorException;
import com.voidsong.eccu.support_classes.EccuCipher;
import com.voidsong.eccu.support_classes.Settings;
import com.voidsong.eccu.support_classes.RequestBodyBuilder;

import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CertificatePinner;
import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

public class Internet {
    public static void Init() throws SecurityErrorException, GeneralSecurityException {

        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;

        trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, new TrustManager[] { trustManager }, null);
        sslSocketFactory = sslContext.getSocketFactory();
        CertificatePinning(sslSocketFactory, trustManager);
    }

    public static void updateWeatherData(HttpUrl url, final FragmentWeather fragment) {
        Log.d("TAGMYTAG", "Internet.updateWD");
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAGMYTAG", "fail");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("TAGMYTAG", "success");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String temperature = jsonObject.getString("temperature");
                    String wind_d = jsonObject.getString("wind_direction");
                    String wind_v = jsonObject.getString("wind_velocity");
                    String comment = jsonObject.getString("comment");
                    fragment.updateData(temperature, wind_d, wind_v, comment);
                } catch (JSONException e) {
                    Log.d("TAGMYTAG", String.valueOf(response.code()));
                }
            }
        });
    }

    public static void updateImage(HttpUrl url, final FragmentCamera fragment) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d("TAGMYTAG", "FAIL");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAGMYTAG", "SUCCESS");
                InputStream inputStream = response.body().byteStream();
                fragment.setImg(BitmapFactory.decodeStream(inputStream));
            }
        });
    }

    public static OkHttpClient getClient() {
        return client;
    }

    public static void post(HttpUrl url, String data) {
        String body = new RequestBodyBuilder()
                .add("data", data)
                .add("hash", EccuCipher.hash(data))
                .build();
        Log.d("TAGMYTAG", "Internet: " + body);
        Log.d("TAGMYTAG", "URL: " + API.SCHEME + Settings.getIp() + url);
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, body))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAGMYTAG", "FAIL");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAGMYTAG", "success");
            }
        });
    }

    private static OkHttpClient client;
    private static MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private static void CertificatePinning(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        //if (client == null) {

            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

            client = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .hostnameVerifier(new CustomHostNameVerifier())
                    .certificatePinner(new CertificatePinner.Builder()
                            .add(Settings.getIp(), "sha256/3nXyfqT2mpHoZbP10u++TiE55PU+FSEDUHZqcb6O5EM=")
                            .build())
                    .cookieJar(new JavaNetCookieJar(cookieManager))
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .build();
        //}
    }

    @NonNull
    private static InputStream trustedCertificatesInputStream() {
        String smartHomeRsaCertificationAuthority = ""
                + "-----BEGIN CERTIFICATE-----\n"
                + "MIIDLDCCAhQCCQCc5CZE1O5YKzANBgkqhkiG9w0BAQsFADBYMQswCQYDVQQGEwJS\n"
                + "VTEWMBQGA1UECAwNQmFzaGtvcnRvc3RhbjEMMAoGA1UEBwwDVWZhMRIwEAYDVQQK\n"
                + "DAlWb2lkIHNvbmcxDzANBgNVBAMMBlNvbGxvczAeFw0xNjA4MTAwNzE2MzJaFw0x\n"
                + "NzA4MTAwNzE2MzJaMFgxCzAJBgNVBAYTAlJVMRYwFAYDVQQIDA1CYXNoa29ydG9z\n"
                + "dGFuMQwwCgYDVQQHDANVZmExEjAQBgNVBAoMCVZvaWQgc29uZzEPMA0GA1UEAwwG\n"
                + "U29sbG9zMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvhP9bVsdUhBl\n"
                + "83ULb7jJdmoJpyRrPLmwrqsVzNtEKPsTT2pziZJNifkOpchshNIKIY/7UcbTMPnt\n"
                + "ZSD8ENuKnaAddHV0dfeYCEHl2+YW7z+sMcwqjQuPV/7hw4S0Am9aW+z99tdYZYXR\n"
                + "Kqflp8NzJO8ndGkFqrqUTMaQYNaonq+iOCa5G8YjvqdsyORfP+bzQZtT3WzKlnl8\n"
                + "9E8lA20c6YQXfKdQgDF/ypYnZ/Y1iHDVAdF3RpI/NJNj2D1fN5+QQksh5fdFsTs7\n"
                + "usQkJyzwU4/sbTHh8hXZ9eh3pgRkdLB+lf9T+jSyCM6sLR7qECweQpys+eTRL24T\n"
                + "g3iJ2RWNRQIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQCg1yle/A1X595l+ZlKIvTD\n"
                + "rSyN5pKA8rAp6+fT9DCu02mM6ToEPOBlUVT2CQJEd60AryKSJQ7IiS0PwwArM9nf\n"
                + "hmX+6v+fy+KC5LeUeAImBtL+/MgLzBXYvd6L6xQtPnfkynN5zMQPg0jkoKKN54s/\n"
                + "H0sS1Jg/NrRG0IiP7Cj530RJfRA6PDLZDVESJ3kJzWNKuhRwrNwDQuqHpwWFI0cv\n"
                + "IlJTpT6GDzxVBAn8RotiNfEG2R6CmjCsBpam7wMyMK2tZ3kLwy6QDaGzTiE3we+g\n"
                + "i/wPahB80NZ380wtnPzOeGtvyMNP9aF9SexhxkQ4Wo6Rj3J/s1b/uhS++tia0dhr\n"
                + "-----END CERTIFICATE-----\n";

        return new Buffer()
                .writeUtf8(smartHomeRsaCertificationAuthority)
                .inputStream();
    }

    private static X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException {

        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        char[] password = "password".toCharArray();
        KeyStore keyStore = newEmptyKeyStore(password);
        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, password);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                    + Arrays.toString(trustManagers));
        }
        return (X509TrustManager) trustManagers[0];
    }

    private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, password); // creates an empty KeyStore
            return keyStore;
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }
}
