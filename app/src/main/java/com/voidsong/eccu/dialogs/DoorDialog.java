package com.voidsong.eccu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

//import com.github.zagum.switchicon.SwitchIconView;
import com.voidsong.eccu.R;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.support_classes.Settings;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DoorDialog extends DialogFragment {

    public static final String ID = "DoorControl";

    public interface IDoorController {
        void setOpenedDoorCount(Integer opened, Integer all);
    }

    private class Door {
        private int _id;
        private LinearLayout _control;
        private boolean _state;

        public void setControl(LinearLayout control) {
            _control = control;
            _control.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // SwitchIcon.toggle();
                    if (_control.getOrientation() == LinearLayout.HORIZONTAL) {
                        _control.setOrientation(LinearLayout.VERTICAL);
                        _state = true;
                        _control.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorRed));
                    } else {
                        _control.setOrientation(LinearLayout.HORIZONTAL);
                        _state = false;
                        _control.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorCyan));
                    }

                    // request to server
                }
            });
        }
        public void verifyState() {
            if (_state) {
                _control.setOrientation(LinearLayout.VERTICAL);
                _control.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorRed));
            } else {
                _control.setOrientation(LinearLayout.HORIZONTAL);
                _control.setBackgroundColor(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.colorCyan));
            }
        }
        public void setState(boolean state) {
            _state = state;
        }
        public boolean getState() {
            return _state;
        }
    }

    private Door door;

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
        final View main_view = inflater.inflate(R.layout.dialog_door, null);
<<<<<<< HEAD

=======
        if (door == null) {
            door = new Door();
            door.setControl((LinearLayout) main_view.findViewById(R.id.door));
        } else {
            door.setControl((LinearLayout) main_view.findViewById(R.id.door));
        }
>>>>>>> origin/feature/gui0
        builder.setView(main_view);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        door.verifyState();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        IDoorController activity = (IDoorController) getActivity();
        activity.setOpenedDoorCount(door.getState() ? 1 : 0, 1);
    }
}
