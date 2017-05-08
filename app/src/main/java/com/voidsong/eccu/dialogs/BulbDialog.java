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
import com.voidsong.eccu.network.Internet;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class BulbDialog extends DialogFragment {

    public static final String ID = "BulbControl";

    private class Bulb {
        Switch control;
        Integer id;
        Boolean state;

        public void setControl(Switch s) {
            control = s;
            control.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d("TAGY", "cahnged to " + isChecked);
                    state = isChecked;
                }
            });
        }
        public void setId(Integer i) {
            id = i;
        }
        public void setState(Boolean b) {
            state = b;
            //control.setChecked(state); // TODO get the state from server
        }
        public Boolean getState() {
            return state;
        }
    }

    private Bulb[] array;

    public interface IBulbController {
        void setActiveBulbCount(Integer active, Integer all);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View main_view = inflater.inflate(R.layout.dialog_bulb, null);
        if (array == null) {
            Log.d("TAGY", "you must see that only once");
            array = new Bulb[2];
            array[0] = new Bulb();
            array[0].setId(0);
            //array[0].setControl((Switch) main_view.findViewById(R.id.Bulb1));
            array[0].setState(false);
            array[1] = new Bulb();
            array[1].setId(1);
            //array[1].setControl((Switch) main_view.findViewById(R.id.Bulb2));
            array[1].setState(false);
        } else {
            //array[0].setControl((Switch) main_view.findViewById(R.id.Bulb1));
            //array[1].setControl((Switch) main_view.findViewById(R.id.Bulb2));
        }

        builder.setView(main_view);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        for (Bulb item : array) {
            item.setState(item.getState()); // TODO change
        }
        Log.d("TAGY", (array[0].getState()) ? "T" : "F");
        Log.d("TAGY", (array[1].getState()) ? "T" : "F");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.d("TAGY", (array[0].getState()) ? "T" : "F");
        Log.d("TAGY", (array[1].getState()) ? "T" : "F");
        IBulbController activity = (IBulbController) getActivity();
        int active = 0;
        for (Bulb item : array)
            if (item.getState())
                active++;
        activity.setActiveBulbCount(active, array.length);
    }
}
