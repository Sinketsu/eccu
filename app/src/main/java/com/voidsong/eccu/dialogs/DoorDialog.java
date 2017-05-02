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

import com.github.zagum.switchicon.SwitchIconView;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View main_view = inflater.inflate(R.layout.dialog_door, null);
        final Button btn = (Button)main_view.findViewById(R.id.btn2);
        final SwitchIconView d = (SwitchIconView)main_view.findViewById(R.id.SW);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                OkHttpClient client = Internet.getClient();
                Request request = new Request.Builder()
                        .url("https://" + Settings.getIp() + "/api/get/light")
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        btn.post(new Runnable() {
                            @Override
                            public void run() {
                                btn.setText("FAIL");
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String g = response.body().string();
                        Log.d("TAGMYTAG", g);
                        btn.post(new Runnable() {
                            @Override
                            public void run() {
                                btn.setText(g);
                            }
                        });
                    }
                });*/
                d.switchState();

            }
        });
        builder.setView(main_view);
        return builder.create();
    }
}
