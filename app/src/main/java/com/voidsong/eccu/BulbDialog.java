package com.voidsong.eccu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by CoolHawk on 4/23/2017.
 */

public class BulbDialog extends DialogFragment {

    ToggleButton toggleButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View main_view = inflater.inflate(R.layout.dialog_bulb, null);
        builder.setView(main_view);
        toggleButton = (ToggleButton) main_view.findViewById(R.id.toggleButton); //TODO delete
        return builder.create();
    }


    /*@Override
    public void onCancel(DialogInterface dialog) {
        //Здеся надо сделать его положительным ответом
        super.onCancel(dialog);
    }*/


}
