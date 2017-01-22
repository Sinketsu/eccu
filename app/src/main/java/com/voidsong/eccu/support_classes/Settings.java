package com.voidsong.eccu.support_classes;


public class Settings {
    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        Settings.ip = ip;
    }

    public static String ip = "192.168.0.105";
}
