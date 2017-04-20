package com.voidsong.eccu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.voidsong.eccu.abstract_classes.RefreshableFragment;
import com.voidsong.eccu.network.Internet;

public class FragmentWeather extends RefreshableFragment {

    static final String ARGUMENT_TEMPERATURE = "temperature";
    static final String ARGUMENT_WIND = "wind";
    static final String ARGUMENT_STATE = "state";

    Integer _temperature;
    String _wind;
    String _state;

    public static FragmentWeather new_instance(Integer temperature, String wind, String state) {
        FragmentWeather fragment = new FragmentWeather();
        Bundle args = new Bundle();
        args.putInt(ARGUMENT_TEMPERATURE, temperature);
        args.putString(ARGUMENT_WIND, wind);
        args.putString(ARGUMENT_STATE, state);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _temperature = getArguments().getInt(ARGUMENT_TEMPERATURE);
        _wind = getArguments().getString(ARGUMENT_WIND);
        _state = getArguments().getString(ARGUMENT_STATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, null);

        ImageView img = (ImageView) view.findViewById(R.id.weather_image);
        TextView textView = (TextView) view.findViewById(R.id.weather);

        return view;
    }

    public void refresh() {
        // TODO change
    }

}
