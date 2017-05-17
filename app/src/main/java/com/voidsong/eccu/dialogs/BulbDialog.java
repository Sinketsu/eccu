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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.github.zagum.switchicon.SwitchIconView;
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
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class BulbDialog extends DialogFragment {

    public static final String ID = "BulbControl";
    private static final int SIZE = 2;

    private class Bulb extends SmartControl {
        @Override
        public void setControl(LinearLayout control,
                               SwitchIconView iconView,
                               String url_get,
                               String url_set) {
            _path_get = url_get;
            _path_set = url_set;
            icon = iconView;
            control.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    icon.switchState();
                    _state = !_state;
                    HttpUrl url = new HttpUrl.Builder()
                            .scheme(API.SCHEME)
                            .host(Settings.getIp())
                            .addPathSegment(_path_set)
                            .build();
                    try {
                        JSONObject json = new JSONObject();
                        json.put("value", icon.isIconEnabled() ? 1 : 0);
                        String data = json.toString();
                        Internet.post(url, data);
                    } catch (JSONException e) {
                    }
                }
            });
        }

        @Override
        public void verifyState() {
            String rnd = String.valueOf(random.nextInt());
            HttpUrl door_url = new HttpUrl.Builder()
                    .scheme(API.SCHEME)
                    .host(Settings.getIp())
                    .addPathSegment(_path_get)
                    .addQueryParameter("rnd", rnd)
                    .addQueryParameter("hash", EccuCipher.hash(rnd))
                    .build();
            Request bulb_request = new Request.Builder()
                    .url(door_url)
                    .build();
            Internet.getClient().newCall(bulb_request).enqueue(new Callback() {
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

    private SecureRandom random = new SecureRandom();

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
            array = new Bulb[SIZE];
            for (int i = 0; i < SIZE; i++)
                array[i] = new Bulb();
        }
        array[0].setControl((LinearLayout)main_view.findViewById(R.id.spotlight),
                (SwitchIconView)main_view.findViewById(R.id.spotlight_icon),
                API.GET_SPOTLIGHT,
                API.SET_SPOTLIGHT);
        array[1].setControl((LinearLayout)main_view.findViewById(R.id.light),
                (SwitchIconView)main_view.findViewById(R.id.light_icon),
                API.GET_LIGHT,
                API.SET_LIGHT);

        builder.setView(main_view);
        return builder.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        for (Bulb bulb : array) {
            bulb.verifyState();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        int current = 0;
        for (Bulb bulb : array)
            if (bulb.get_state())
                current++;
        IBulbController activity = (IBulbController)getActivity();
        activity.setActiveBulbCount(current, SIZE);
    }
}
