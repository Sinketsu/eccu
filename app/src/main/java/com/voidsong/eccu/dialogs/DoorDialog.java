package com.voidsong.eccu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

//import com.github.zagum.switchicon.SwitchIconView;
import com.voidsong.eccu.R;
import com.voidsong.eccu.abstract_classes.SmartControl;
import com.voidsong.eccu.network.API;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.support_classes.EccuCipher;
import com.voidsong.eccu.support_classes.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DoorDialog extends DialogFragment {

    public static final String ID = "DoorControl";

    public interface IDoorController {
        void setOpenedDoorCount(Integer opened, Integer all);
    }

    private class Door extends SmartControl {
        public void setControl(LinearLayout control,
                               //SwitchIconView,
                               final String url_get,
                               final String url_set) {
            _path_get = url_get;
            _path_set = url_set;
            control.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // icon.switchstate();
                    // request to server
                    /*
                    HttpUrl url = new HttpUrl.Builder()
                            .scheme(API.SCHEME)
                            .host(Settings.getIp())
                            .addPathSegment(_path_set)
                            .build();
                    try {
                        JSONObject json = new JSONObject();
                        json.put("value", icon.is_enabled() ? 1 : 0);
                        String data = json.toString();
                        Internet.post(url, data);
                    } catch (JSONException e) {
                    }*/
                }
            });
        }
        public void verifyState() {
            String rnd = String.valueOf(random.nextInt());
            HttpUrl door_url = new HttpUrl.Builder()
                    .scheme(API.SCHEME)
                    .host(Settings.getIp())
                    .addPathSegment(_path_get)
                    .addQueryParameter("rnd", rnd)
                    .addQueryParameter("hash", EccuCipher.hash(rnd))
                    .build();
            Request door_request = new Request.Builder()
                    .url(door_url)
                    .build();
            Internet.getClient().newCall(door_request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.code() == 200) {
                        final int state = Integer.valueOf(response.body().string());
                        set_state(state == 1);
                    }
                }
            });
        }
    }

    private Door door;
    private SecureRandom random = new SecureRandom();

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

        if (door == null) {
            door = new Door();
        }
        door.setControl((LinearLayout)main_view.findViewById(R.id.door),
                //(SwitchIconView)main_view.findViewById(R.id.icon),
                API.GET_DOOR,
                API.SET_DOOR);
        builder.setView(main_view);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //door.verifyState();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        IDoorController activity = (IDoorController) getActivity();
        activity.setOpenedDoorCount(door.get_state() ? 1 : 0, 1);
    }
}
