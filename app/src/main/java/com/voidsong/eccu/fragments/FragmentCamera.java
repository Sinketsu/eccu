package com.voidsong.eccu.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.voidsong.eccu.R;
import com.voidsong.eccu.abstract_classes.RefreshableFragment;
import com.voidsong.eccu.network.API;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.support_classes.EccuCipher;
import com.voidsong.eccu.support_classes.Settings;

import java.security.SecureRandom;

import okhttp3.HttpUrl;

public class FragmentCamera extends RefreshableFragment {

    static final String ARGUMENT_IMAGE_SRC = "image_src";
    static final String ARGUMENT_AVAILABLE = "available";

    private SecureRandom random = new SecureRandom();

    public interface IFragmentCameraControl {
        void stopProgress();
    }

    int _img_id;
    boolean _available;
    ImageView img;

    public void setImg(final Bitmap bitmap) {
        img.post(new Runnable() {
            @Override
            public void run() {
                img.setImageBitmap(bitmap);
            }
        });
        IFragmentCameraControl activity = (IFragmentCameraControl) getActivity();
        activity.stopProgress();
    }

    @Override
    public boolean is_available() {
        return _available;
    }

    public static FragmentCamera new_instance(int img_id, boolean available) {
        FragmentCamera fragment = new FragmentCamera();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_IMAGE_SRC, img_id);
        args.putBoolean(ARGUMENT_AVAILABLE, available);
        fragment.setArguments(args);
        return fragment;
    }

    public void refresh() {
        if (_available) {
            String rnd = String.valueOf(random.nextInt());
            HttpUrl url = new HttpUrl.Builder()
                    .scheme(API.SCHEME)
                    .host(Settings.getIp())
                    .addPathSegment(API.CAMERA)
                    .addQueryParameter("rnd", rnd)
                    .addQueryParameter("hash", EccuCipher.hash(rnd))
                    .build();
            Internet.updateImage(url, this);
        } else {
            ((IFragmentCameraControl)getActivity()).stopProgress();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _img_id = getArguments().getInt(ARGUMENT_IMAGE_SRC);
        _available = getArguments().getBoolean(ARGUMENT_AVAILABLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, container, false);

        img = (ImageView) view.findViewById(R.id.image);
        img.setImageResource(_img_id);

        return view;
    }
}
