package com.voidsong.eccu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.voidsong.eccu.R;
import com.voidsong.eccu.support_classes.Settings;

public class CipherDialog extends DialogFragment {

    public static final String ID = "CipherDialog";

    private EditText salt;
    private EditText iv;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.offer_to_input_iv_and_hash_salt));
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View main_view = inflater.inflate(R.layout.dialog_cipher, null);
        salt = (EditText) main_view.findViewById(R.id.salt);
        iv = (EditText) main_view.findViewById(R.id.iv);
        builder.setView(main_view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        String hash_salt = "ECCU";
        String IV = "ECCU:SECRET_IV!!";
        if (!salt.getText().toString().isEmpty())
            hash_salt = salt.getText().toString();
        if (!iv.getText().toString().isEmpty())
            IV = iv.getText().toString();
        Settings.setHash_salt(hash_salt);
        Settings.setIV(IV.getBytes());
    }
}
