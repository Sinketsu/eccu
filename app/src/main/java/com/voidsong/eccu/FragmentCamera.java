package com.voidsong.eccu;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.voidsong.eccu.abstract_classes.RefreshableFragment;

import java.util.Random;

public class FragmentCamera extends RefreshableFragment {

    static final String ARGUMENT_IMAGE_SRC = "image_src";
    static final String ARGUMENT_AVAILABLE = "available";

    int _img_id;
    boolean _available;
    ImageView img;

    public void setImg(Bitmap bitmap) {
        img.setImageBitmap(bitmap);
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
            int i = new Random().nextInt(3);
            if (i == 0)
                img.setImageResource(R.drawable.fon);
            else if (i == 1)
                img.setImageResource(R.drawable.logo);
            else if (i == 2)
                img.setImageResource(R.drawable.set);
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
        View view = inflater.inflate(R.layout.fragment_camera, null);

        img = (ImageView) view.findViewById(R.id.image);
        img.setImageResource(_img_id);

        return view;
    }
}
