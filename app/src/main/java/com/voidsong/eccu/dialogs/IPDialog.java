package com.voidsong.eccu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.voidsong.eccu.R;
import com.voidsong.eccu.support_classes.Settings;

public class IPDialog extends DialogFragment{

    public static final String ID = "IPDialog";

    private EditText editText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Enter the server IP address");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View main_view = inflater.inflate(R.layout.dialog_login, null);
        editText = (EditText)  main_view.findViewById(R.id.ipaddress);
        editText.setText(Settings.getIp());
        builder.setView(main_view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            editText = (EditText)  main_view.findViewById(R.id.ipaddress);
                        }catch (NullPointerException e) {
                            e.printStackTrace(); //TODO CHANGE
                        }
                        setIP();
                        IPDialog.this.getDialog().dismiss();
                    }
                })
                .setNegativeButton(R.string.Cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        IPDialog.this.getDialog().cancel();
                    }
                });


        return builder.create();
    }


    public void setIP(){
        Settings.setIp(editText.getText().toString());
    }


}

