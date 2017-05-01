package com.voidsong.eccu.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.graphics.Palette;

import com.voidsong.eccu.R;
import com.voidsong.eccu.abstract_classes.RefreshableFragment;
import com.voidsong.eccu.network.API;
import com.voidsong.eccu.network.Internet;
import com.voidsong.eccu.support_classes.Settings;

public class FragmentWeather extends RefreshableFragment {

    static final String ARGUMENT_AVAILABLE = "available";

    public interface IFragmentWeatherControl {
        void stopProgress();
    }

    ImageView img;
    boolean _available;

    TextView _temperature_tv;
    TextView _wind_velocity_tv;
    TextView _wind_direction_tv;
    TextView _state_tv;

    public static FragmentWeather new_instance(boolean available) {
        FragmentWeather fragment = new FragmentWeather();
        Bundle args = new Bundle();
        args.putBoolean(ARGUMENT_AVAILABLE, available);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _available = getArguments().getBoolean(ARGUMENT_AVAILABLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container);

        img = (ImageView) view.findViewById(R.id.weather_image);
        _temperature_tv = (TextView) view.findViewById(R.id.temperature);
        _state_tv = (TextView) view.findViewById(R.id.comment);
        _wind_velocity_tv = (TextView) view.findViewById(R.id.wind_velocity);
        _wind_direction_tv = (TextView) view.findViewById(R.id.wind_direction);

        return view;
    }

    public void refresh() {
        Internet.updateWeatherData(API.SCHEME + Settings.getIp() + API.WEATHER, this);
    }

    public void updateData(String temperature, final String wind_d, final String wind_v, final String comment) {
        final String text = temperature + getString(R.string.one_space) + getString(R.string.degree);
        setImg(R.drawable.fon);

        _temperature_tv.post(new Runnable() {

            @Override
            public void run() {
                _temperature_tv.setText(text);
            }
        });

        _wind_direction_tv.post(new Runnable() {

            @Override
            public void run() {
                _wind_direction_tv.setText(wind_d);
            }
        });

        _wind_velocity_tv.post(new Runnable() {

            @Override
            public void run() {
                _wind_velocity_tv.setText(wind_v);
            }
        });

        _state_tv.post(new Runnable() {

            @Override
            public void run() {
                _state_tv.setText(comment);
            }
        });

        switch (comment) {
            case "пасмурно":
                setImg(R.drawable.weather_overcast);
                break;
            case "небольшой дождь":
                setImg(R.drawable.weather_small_rain);
                break;
            case "облачно с прояснениями":
                setImg(R.drawable.weather_cloudy);
                break;
            case "ясно":
                setImg(R.drawable.weather_clear);
                break;
            case "малооблачно":
                setImg(R.drawable.weather_cloudy);
                break;
            case "сильный дождь":
                setImg(R.drawable.weather_rain);
                break;
            case "дождь":
                setImg(R.drawable.weather_rain);
                break;
            case "снег":
                setImg(R.drawable.weather_snow);
                break;
            case "снегопад":
                setImg(R.drawable.weather_snow);
                break;
            case "небольшой снег":
                setImg(R.drawable.weather_small_snow);
                break;
            case "дождь со снегом":
                setImg(R.drawable.weather_snow_with_rain);
                break;
            default:
                setImg(R.drawable.weather_default);
        }

    }

    @Override
    public boolean is_available() {
        return _available;
    }


    private void setImg(int id) {
        final Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);

        img.post(new Runnable() {
            @Override
            public void run() {
                img.setImageBitmap(bitmap);
            }
        });

        final Palette palette = Palette.from(bitmap).generate();
        final Integer color = palette.getVibrantColor(Color.rgb(255, 255, 255));

        _state_tv.post(new Runnable() {

            @Override
            public void run() {
                _state_tv.setTextColor(color);
            }
        });

        _wind_direction_tv.post(new Runnable() {

            @Override
            public void run() {
                _wind_direction_tv.setTextColor(color);
            }
        });

        _wind_velocity_tv.post(new Runnable() {

            @Override
            public void run() {
                _wind_velocity_tv.setTextColor(color);
            }
        });

        _temperature_tv.post(new Runnable() {

            @Override
            public void run() {
                _temperature_tv.setTextColor(color);
            }
        });

        IFragmentWeatherControl activity = (IFragmentWeatherControl) getActivity();
        activity.stopProgress();
    }
}
