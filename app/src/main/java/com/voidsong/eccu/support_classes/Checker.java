package com.voidsong.eccu.support_classes;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Checker {

    public static boolean isCHECKED() {
        return CHECKED;
    }

    public static boolean detectRoot() {
        return HAS_ROOT;
    }

    public static boolean detectDebug() {
        return IS_UNDER_DEBUG;
    }

    public static boolean detectEmulator() {
        return IS_IN_EMULATOR;
    }

    public static boolean user_has_saved_password(Context context) {
        if (!SAVED_PASSWORD_CHECKED) {
            try {
                FileInputStream fin = context.openFileInput("saved_keys");
                HAS_SAVED_PASSWORDS = true;
                fin.close();
            } catch (IOException e) {
                HAS_SAVED_PASSWORDS = false;
            }
            SAVED_PASSWORD_CHECKED = true;
        }
        return HAS_SAVED_PASSWORDS;
    }

    public static boolean is_first_run(Context context) {
        File info = new File(context.getFilesDir(), "info");
        if (info.exists()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Performs a full test and sets the flags.
     */
    public static void check(Context context) {
        HAS_ROOT = isRooted();
        IS_UNDER_DEBUG = isDebuggable(context);
        IS_IN_EMULATOR = isRunningEmulator();

        CHECKED = true;
    }

    /**
     * Try to determine whether running on a debugger or DEBUG mode.
     *
     * @return true when the application seems to run on DEBUG mode, otherwise false.
     */
    public static boolean isDebuggable(Context context) {
        return 0 != (context.getApplicationInfo().flags &=
                ApplicationInfo.FLAG_DEBUGGABLE);
    }

    /**
     * Try to determine whether running on an emulator.
     *
     * @return true when the application seems to run on an emulator, otherwise false.
     */
    public static boolean isRunningEmulator() {
        if (Build.BRAND.equalsIgnoreCase("generic")) {
            return true;
        } else if (Build.MODEL.contains("google_sdk") || Build.MODEL.contains("Emulator") ||
                Build.MODEL.contains("Android SDK")) {
            return true;
        } else if (Build.PRODUCT.contains("sdk") || Build.PRODUCT.equalsIgnoreCase("full_x86")) {
            return true;
        } else if (Build.HARDWARE.contains("goldfish")) {
            return true;
        }
        return false;
    }

    /**
     * Try to determine whether running on a rooted device.
     *
     * @return true when the app seems to run on a rooted device, otherwise false.
     */
    public static boolean isRooted() {
        if (isRootedSigningKeys()) {
            return true;
        } else if (isRootedBinariesPresent()) {
            return true;
        }
        return false;
    }

    private static boolean CHECKED = false;

    private static boolean HAS_ROOT;
    private static boolean IS_UNDER_DEBUG;
    private static boolean IS_IN_EMULATOR;

    private static boolean SAVED_PASSWORD_CHECKED = false;
    private static boolean HAS_SAVED_PASSWORDS;

    private static boolean isRootedSigningKeys() {
        final String buildTags = Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }
        return false;
    }

    private static boolean isRootedBinariesPresent() {
        String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
                "/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/",
                "/system/app/"};
        String[] binaries = {"Superuser", "su", "busybox"};
        for (String binary : binaries) {
            if (binaryExists(places, binary)) {
                return true;
            }
        }
        return false;
    }

    private static boolean binaryExists(final String[] paths, final String binary) {
        for (String path : paths) {
            if (fileExists(path + binary)) {
                return true;
            }
        }
        return false;
    }

    private static boolean fileExists(final String filePath) {
        try {
            final File file = new File(filePath);
            if (file.exists()) {
                return true;
            }
        } catch (NullPointerException npe) {
            return false;
        }
        return false;
    }
}
