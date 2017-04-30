package com.voidsong.eccu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.voidsong.eccu.R;
import com.voidsong.eccu.network.API;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.support_classes.Settings;

public class InfoDialog extends DialogFragment {

    public static final String ID = "InfoControl";

    public interface IInfoController {
        void setTemperature(Integer temperature);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View main_view = inflater.inflate(R.layout.dialog_info, null);
        Button btn = (Button) main_view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAGMYTAG", "clicked"); // TODO delete
                String url = API.SCHEME + Settings.getIp() + API.LIGHT;
                Log.d("TAGMYTAG", "url: " + url);
                String data = "{\"value\":1}";
                Log.d("TAGMYTAG", "data: " + data);
                Internet.post(url, data);
                Log.d("TAGMYTAG", "finish");
            }
        });
        builder.setView(main_view);
        return builder.create();
    }
}
