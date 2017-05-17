package com.voidsong.eccu;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import java.security.GeneralSecurityException;

import com.dd.CircularProgressButton;
import com.voidsong.eccu.dialogs.CipherDialog;
import com.voidsong.eccu.network.User;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.exceptions.SecurityErrorException;
import com.voidsong.eccu.support_classes.EccuCipher;
import com.voidsong.eccu.support_classes.Settings;
import com.voidsong.eccu.support_classes.Checker;
import com.voidsong.eccu.support_classes.StringWorker;
import com.voidsong.eccu.dialogs.IPDialog;

import android.support.v4.app.DialogFragment;

import yellow5a5.actswitchanimtool.ActSwitchAnimTool;

public class LoginActivity extends AppCompatActivity implements User.ILogin {

    private final String TAG = "ECCU/LoginActivity";

    private EditText loginText;
    private EditText passwordText;
    //private AppCompatButton button;
    private CircularProgressButton button;
    private AppCompatCheckBox checkbox;

    private LoginActivity activity = this;

    private Snackbar snackbar;
    private View.OnClickListener snackbarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            snackbar.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Checker.check(getApplicationContext());
        if (Checker.detectDebug()) {
            Log.d(TAG, "detecting debug");
        }
        if (Checker.detectEmulator()) {
            Log.d(TAG, "detecting emulator");
        }
        if (Checker.detectRoot()) {
            Log.d(TAG, "detecting root");
        }

        EccuCipher.setContext(getApplicationContext());

        Settings.setContext(getApplicationContext());
        Settings.loadInfo();

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        loginText = (EditText)findViewById(R.id.input_login);
        passwordText = (EditText)findViewById(R.id.input_password);
        button = (CircularProgressButton) findViewById(R.id.btn_login);
        checkbox = (AppCompatCheckBox)findViewById(R.id.saved_passwd_checkbox);

        button.setIndeterminateProgressMode(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setProgress(50);
                try {
                    Internet.Init();
                } catch (SecurityErrorException | GeneralSecurityException e) {
                    snackbar = Snackbar.make(button, getResources().getString(R.string.security_errors),
                            Snackbar.LENGTH_INDEFINITE)
                            .setActionTextColor(Color.WHITE)
                            .setAction(getResources().getString(R.string.ok), snackbarClickListener);
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
                    snackbar.show();
                    stop_progress();
                    return;
                }
                Log.d(TAG, "Internet client initialized.");

                String login = loginText.getText().toString();
                String password = passwordText.getText().toString();

                Settings.setState(checkbox.isChecked());
                Settings.saveInfo(login);
                User.authenticate(login, password, activity);
            }
        });

        if (Settings.getIV() == null ||
                !Settings.ifHasHash()) {
            DialogFragment newFragment = new CipherDialog();
            newFragment.show(getSupportFragmentManager(), CipherDialog.ID);
        }

        checkbox.setChecked(Settings.getState());

        if (StringWorker.equals(Settings.getIp(), Settings.DEFAULT_IP)) {
            snackbar = Snackbar.make(button, getResources().getString(R.string.offer_to_enter_ip),
                    Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(Color.WHITE)
                    .setAction(getResources().getString(R.string.ok), snackbarClickListener);
            View view = snackbar.getView();
            view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
            snackbar.show();
        }
        loginText.setText(Settings.getLogin());
        if (Checker.user_has_saved_password(getApplicationContext())) {
            Settings.load_saved_passwords();
            passwordText.setText(Settings.getSaved_passwd());
        }
    }

    public void IPImageButton(View view){
        DialogFragment newFragment = new IPDialog();
        newFragment.show(getSupportFragmentManager(), IPDialog.ID);
    }

    @Override
    public void login(String password) {
        String _status = User.getStatus();
        Log.d(TAG, "status of authorization: " + _status);

        if (StringWorker.equals(_status, User.OK)) {
            if (!Checker.user_has_saved_password(getApplicationContext())) {
                Settings.load(password);
            }
            if (checkbox.isChecked()) {
                Settings.setSaved_passwd(password);
                Settings.save_saved_passwords();
            } else {
                Settings.save(password);
            }
            stop_progress();
            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);

        } else if (StringWorker.equals(_status, User.INVALID_PASSWORD)) {
            snackbar = Snackbar.make(button, getResources().getString(R.string.invalid_login_password),
                    Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.WHITE)
                    .setAction(getResources().getString(R.string.ok), snackbarClickListener);
            View view = snackbar.getView();
            view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
            stop_progress();
            snackbar.show();
        } else {
            snackbar = Snackbar.make(button, getResources().getString(R.string.connection_issues),
                    Snackbar.LENGTH_LONG)
                    .setActionTextColor(Color.WHITE)
                    .setAction(getResources().getString(R.string.ok), snackbarClickListener);
            View view = snackbar.getView();
            view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorRed));
            stop_progress();
            snackbar.show();
        }
    }

    @Override
    public void stop_progress() {
        button.post(new Runnable() {
            @Override
            public void run() {
                button.setProgress(0);
            }
        });
    }
}
