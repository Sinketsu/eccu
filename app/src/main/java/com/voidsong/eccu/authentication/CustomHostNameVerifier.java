package com.voidsong.eccu.authentication;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import com.voidsong.eccu.support_classes.Settings;
import com.voidsong.eccu.support_classes.StringWorker;

public class CustomHostNameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return StringWorker.equals(Settings.getIp(), hostname);
    }
}
