package com.voidsong.eccu.support_classes;


public class Settings {

    public static int save() {
        return 0;
    }

    public static int load() {
        return 0;
    }

    public static String getIp() {
        return ip;
    }

    public static void setIp(String ip) {
        Settings.ip = ip;
    }

    private static String ip = "192.168.0.106";
}
