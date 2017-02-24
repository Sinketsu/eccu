package com.voidsong.eccu;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;

import com.voidsong.eccu.network.User;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.exceptions.SecurityErrorException;
import com.voidsong.eccu.support_classes.Settings;
import com.voidsong.eccu.support_classes.Checker;
import com.voidsong.eccu.support_classes.StringWorker;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.SecretKeySpec;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "ECCU/LoginActivity";

    private EditText login;
    private EditText password;
    private AppCompatButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Checker.check(getApplicationContext());

        if (Checker.detectDebug()) {
            Log.d(TAG, "detecting debug"); // TODO change
        }

        if (Checker.detectEmulator()) {
            Log.d(TAG, "detecting emulator"); // TODO change
        }

        if (Checker.detectRoot()) {
            Log.d(TAG, "detecting root"); // TODO change
        }

        login = (EditText)findViewById(R.id.input_login);
        password = (EditText)findViewById(R.id.input_password);
        button = (AppCompatButton)findViewById(R.id.btn_login);

        try {
            Internet.Init();
        } catch (SecurityErrorException | GeneralSecurityException e) {
            e.printStackTrace(); // TODO change
        }

        Settings.setContext(getApplicationContext());

        if (!Checker.is_first_run(getApplicationContext())) {
            Settings.loadInfo();
        }

        if (StringWorker.equals(Settings.getIp(), "")) {
            // TODO show dialog for input ap address
        }

        //login.setText(Settings.getLogin());

        if (Checker.has_saved_password(getApplicationContext())) {

        }
    }
}
