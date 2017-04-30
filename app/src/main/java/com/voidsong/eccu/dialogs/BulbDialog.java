package com.voidsong.eccu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.voidsong.eccu.R;

import static android.content.ContentValues.TAG;

public class BulbDialog extends DialogFragment {

    public static final String ID = "BulbControl";

    Switch bulb1;
    Bundle args = new Bundle();

    private boolean flag = false;

    public interface IBulbController {
        void setActiveBulbCount(Integer active, Integer all);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setNegativeButton("НИХТ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View main_view = inflater.inflate(R.layout.dialog_bulb, null);
        bulb1 = (Switch) main_view.findViewById(R.id.Bulb1);
        bulb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                flag = isChecked;
            }
        });
        builder.setView(main_view);
        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("heyhey", "onCreate: before if");
        if (args != null) {
            Log.d("heyhey", "onCreate: after if");
            bulb1.setChecked(args.getBoolean("MYKEY"));
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("heyhey", "onCreate : before if");

        if (savedInstanceState != null) {
            Log.d("heyhey", "onCreate : after if");
            bulb1.setChecked(savedInstanceState.getBoolean("MYKEY"));
        }
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("MYKEY", flag);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.d("heyhey", "onCancel: ");
        //dismissAllowingStateLoss();
        super.onCancel(dialog);
    }

    @Override
    public void onPause() {
        super.onPause();
        args.putBoolean("MYKEY", flag);
        Log.d("heyhey", "we in on Pause");
    }

}
