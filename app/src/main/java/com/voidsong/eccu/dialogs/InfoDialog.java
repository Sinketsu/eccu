package com.voidsong.eccu.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.voidsong.eccu.R;
import com.voidsong.eccu.network.API;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.support_classes.EccuCipher;
import com.voidsong.eccu.support_classes.Settings;
import com.xw.repo.BubbleSeekBar;

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

public class InfoDialog extends DialogFragment {

    public static final String ID = "InfoControl";

    private BubbleSeekBar seekBar;
    private TextView textView;

    private SecureRandom random = new SecureRandom();

    public interface IInfoController {
        void setTemperature(Integer temperature);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                IInfoController activity = (IInfoController) getActivity();
                                activity.setTemperature(seekBar.getProgress());
                                HttpUrl url = new HttpUrl.Builder()
                                        .scheme(API.SCHEME)
                                        .host(Settings.getIp())
                                        .addPathSegment(API.SET_SETTED_TEMPERATURE)
                                        .build();
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("value", seekBar.getProgress());
                                    String data = jsonObject.toString();
                                    Internet.post(url, data);
                                } catch (JSONException e) {
                                    // TODO change
                                }
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.Cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View main_view = inflater.inflate(R.layout.dialog_info, null);

        seekBar = (BubbleSeekBar) main_view.findViewById(R.id.seekbar);
        textView = (TextView) main_view.findViewById(R.id.temperature_text_view);
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListenerAdapter() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {
                String text = String.valueOf(progress) + " " + getResources().getString(R.string.degree);
                textView.setText(text);
            }
        });
        update_from_server();
        builder.setView(main_view);
        return builder.create();
    }

    private void update_from_server() {
        String rnd = String.valueOf(random.nextInt());
        HttpUrl temperature_url = new HttpUrl.Builder()
                .scheme(API.SCHEME)
                .host(Settings.getIp())
                .addPathSegment(API.GET_SETTED_TEMPERATURE)
                .addQueryParameter("rnd", rnd)
                .addQueryParameter("hash", EccuCipher.hash(rnd))
                .build();
        Request temperature_request = new Request.Builder()
                .url(temperature_url)
                .build();
        Internet.getClient().newCall(temperature_request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final int temperature = Integer.valueOf(response.body().string());
                seekBar.post(new Runnable() {
                    @Override
                    public void run() {
                        seekBar.setProgress(temperature);
                    }
                });
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        String text = String.valueOf(temperature) + " " + getResources().getString(R.string.degree);
                        textView.setText(text);
                    }
                });
            }
        });
    }
}
