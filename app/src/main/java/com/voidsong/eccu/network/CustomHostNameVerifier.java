package com.voidsong.eccu.network;

import android.util.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import com.voidsong.eccu.support_classes.Settings;
import com.voidsong.eccu.support_classes.StringWorker;

public class CustomHostNameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        Log.d("TAGMYTAG", hostname);
        //return true; // TODO CHANGE!!!!!
        return StringWorker.equals(Settings.getIp(), hostname);
    }
}
