package com.voidsong.eccu;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.voidsong.eccu.support_classes.StringWorker;


public class StringWorkerTest {

    @Test
    public void stringWorker_equals_TwoSameStrings_ReturnsTrue() {
        assertEquals("Сравнение двух одинаковых строк",
                StringWorker.equals("Hello", new String("Hello")), true);
        assertEquals("Сравнение двух одинаковых строк",
                StringWorker.equals("Hello", "Hello"), true);
        assertEquals("Сравнение двух одинаковых пустых строк",
                StringWorker.equals("", ""), true);
    }

    @Test
    public void stringWorker_equals_TwoDifferentStrings_ReturnsFalse() {
        assertEquals("Сравнение двух различных строк одинаковой длины",
                StringWorker.equals("Hello", "world"), false);
        assertEquals("Сравнение двух одинаковых строк различной длины",
                StringWorker.equals("Hello", "Hell"), false);
        assertEquals("Сравнение двух различных строк различной длины",
                StringWorker.equals("Hello", "Hi!"), false);
    }

}
