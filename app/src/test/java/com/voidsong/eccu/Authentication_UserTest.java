package com.voidsong.eccu;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.security.GeneralSecurityException;

import com.voidsong.eccu.authentication.User;
import com.voidsong.eccu.exceptions.SecurityErrorException;
import com.voidsong.eccu.authentication.CustomHostNameVerifier;
import com.voidsong.eccu.support_classes.Settings;

public class Authentication_UserTest {

    @Test
    public void authentication_User_Init() throws SecurityErrorException, GeneralSecurityException {
        User.Init();
        assertNotNull(User.getClient());
        // TODO add tests
    }

    @Test
    public void customHostNameVerifier_verify() {
        CustomHostNameVerifier verifier = new CustomHostNameVerifier();
        assertTrue(verifier.verify(Settings.getIp(), null));
        assertFalse(verifier.verify(Settings.getIp() + "1", null));
    }
}
