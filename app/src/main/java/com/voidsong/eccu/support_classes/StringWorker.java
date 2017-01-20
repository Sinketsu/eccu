package com.voidsong.eccu.support_classes;

/**
 * Вспомогательный класс для работы со строками.
 * Обеспечивает безопасные операции сравнения строк, устойчивые к атакам по времени.
 */
public class StringWorker {

    /**
     * Сравнивает две строки.
     * Возвращает true, если строки равны и false в противном случае.
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
