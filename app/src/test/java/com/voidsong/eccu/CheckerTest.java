package com.voidsong.eccu;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import com.voidsong.eccu.support_classes.Checker;

public class CheckerTest {

    @Test
    public void checker_check() {
        //assertEquals("Проверка на эмулятор", Checker.isRunningEmulator(), false);
        assertEquals("Проверка на root", Checker.isRooted(), false);
    }

}
