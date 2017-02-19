package com.voidsong.eccu.support_classes;

/**
 * Support class for working with Strings.
 * Ensures safe operations String comparisons that are resistant to the side-channel attacks by time.
 */
public class StringWorker {

    /**
     * Compares two Strings.
     *
     * @return true when first String equals to second String, otherwise false.
     */
    public static boolean equals(CharSequence first, CharSequence second) {
        int flag = first.length() ^ second.length();

        int length = Math.min(first.length(), second.length());
        for(int i = 0; i < length; i++) {
            flag |= first.charAt(i) ^ second.charAt(i);
        }

        return flag == 0;
    }

}
