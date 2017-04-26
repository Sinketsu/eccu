package com.voidsong.eccu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ToggleButton;

import com.voidsong.eccu.R;

public class BulbDialog extends DialogFragment {

    public interface IBulbController {
        void setActiveBulbCount(Integer active, Integer all);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View main_view = inflater.inflate(R.layout.dialog_bulb, null);
        builder.setView(main_view);
        return builder.create();
    }


    /*@Override
    public void onCancel(DialogInterface dialog) {
        //Здеся надо сделать его положительным ответом
        super.onCancel(dialog);
    }*/


}
