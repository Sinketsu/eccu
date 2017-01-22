package com.voidsong.eccu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.voidsong.eccu.authentication.User;
import com.voidsong.eccu.exceptions.SecurityErrorException;

import java.security.GeneralSecurityException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            User.Init();
            User.Test_request();
        } catch (SecurityErrorException | GeneralSecurityException e) {
            e.printStackTrace();
            // TODO change
        }
    }
}
