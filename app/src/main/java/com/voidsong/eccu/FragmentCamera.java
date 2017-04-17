package com.voidsong.eccu;

import android.support.v4.app.Fragment;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FragmentCamera extends Fragment{

    static final String ARGUMENT_IMAGE_SRC = "image_src";

    int _img_id;

    public static FragmentCamera new_instance(int img_id) {
        FragmentCamera fragment = new FragmentCamera();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_IMAGE_SRC, img_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _img_id = getArguments().getInt(ARGUMENT_IMAGE_SRC);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_camera, null);

        ImageView img = (ImageView) view.findViewById(R.id.image);
        img.setImageResource(_img_id);

        return view;
    }
}
