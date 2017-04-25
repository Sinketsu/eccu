package com.voidsong.eccu;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.security.GeneralSecurityException;

import com.voidsong.eccu.network.User;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.exceptions.SecurityErrorException;
import com.voidsong.eccu.support_classes.Settings;
import com.voidsong.eccu.support_classes.Checker;
import com.voidsong.eccu.support_classes.StringWorker;
import com.voidsong.eccu.support_classes.IPDialog;

import android.support.v4.app.DialogFragment;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = "ECCU/LoginActivity";

    private EditText loginText;
    private EditText passwordText;
    private AppCompatButton button;
    private AppCompatCheckBox checkbox;

    private Snackbar snackbar;
    private View.OnClickListener snackbarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            snackbar.dismiss();
        }
    };

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
            // TODO Show notification about the lack of security
        }

        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
        loginText = (EditText)findViewById(R.id.input_login);
        passwordText = (EditText)findViewById(R.id.input_password);
        button = (AppCompatButton)findViewById(R.id.btn_login);
        checkbox = (AppCompatCheckBox)findViewById(R.id.saved_passwd_checkbox);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String login = loginText.getText().toString();
                String password = passwordText.getText().toString();

                Settings.saveInfo(login);

                User.authenticate(login, password);
                // TODO showing loading circle
                while(StringWorker.equals(User.getStatus(), "")) {
                }
                String _status = User.getStatus();
                if (StringWorker.equals(_status, "OK")) {
                    if (!Checker.user_has_saved_password(getApplicationContext())) {
                        Settings.load(password);
                    }
                    if (checkbox.isChecked()) {
                        Settings.save_saved_passwords();
                    } else {
                        Settings.save(password);
                    }

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (StringWorker.equals(_status, "INVALIDPASSWORD")) {
                    snackbar = Snackbar.make(button, "Invalid login/password", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.WHITE)
                            .setAction("OK", snackbarClickListener);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    snackbar.show();
                } else {
                    snackbar = Snackbar.make(button, "Connection issues", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.WHITE)
                            .setAction("OK", snackbarClickListener);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    snackbar.show();
                }

            }
        });

        try {
            Internet.Init();
        } catch (SecurityErrorException | GeneralSecurityException e) {
            e.printStackTrace(); // TODO change
        }

        Settings.setContext(getApplicationContext());

        Settings.loadInfo();

        if (StringWorker.equals(Settings.getIp(), "")) {
            // TODO show dialog for input ap address
        }

        loginText.setText(Settings.getLogin());
        Log.d("TOG1", "hi0");
        if (Checker.user_has_saved_password(getApplicationContext())) {
            Log.d("TOG1", "hi");
            Settings.load_saved_passwords();
            //Log.d("TOG1", Settings.getSaved_passwd());
            passwordText.setText(Settings.getSaved_passwd());
            // TODO show notification about weak security.
        }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void IPImageButton(View view){
        DialogFragment newFragment = new IPDialog();
        newFragment.show(getSupportFragmentManager(), "IPSettings");
    }
}
