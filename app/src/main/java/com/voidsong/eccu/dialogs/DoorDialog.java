package com.voidsong.eccu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.voidsong.eccu.R;

public class DoorDialog extends DialogFragment {

    public static final String ID = "DoorControl";

    public interface IDoorController {
        void setOpenedDoorCount(Integer opened, Integer all);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View main_view = inflater.inflate(R.layout.dialog_door, null);
        builder.setView(main_view);
        return builder.create();
    }
}
