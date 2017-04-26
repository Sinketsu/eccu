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
import com.voidsong.eccu.network.Internet;

public class FragmentWeather extends RefreshableFragment {

    /*
    static final String ARGUMENT_TEMPERATURE = "temperature";
    static final String ARGUMENT_WIND_DIRECTION = "wind_d";
    static final String ARGUMENT_WIND_VELOCITY = "wind_v";
    static final String ARGUMENT_STATE = "state";
    */
    static final String ARGUMENT_AVAILABLE = "available";

    /*
    Integer _temperature;
    String _wind_velocity;
    String _wind_direction;
    String _state;
    */
    ImageView img;
    boolean _available;

    TextView _temperature_tv;
    TextView _wind_velocity_tv;
    TextView _wind_direction_tv;
    TextView _state_tv;

    public static FragmentWeather new_instance(boolean available) {
        FragmentWeather fragment = new FragmentWeather();
        Bundle args = new Bundle();
        /*
        args.putInt(ARGUMENT_TEMPERATURE, temperature);
        args.putString(ARGUMENT_WIND_DIRECTION, wind_d);
        args.putString(ARGUMENT_WIND_VELOCITY, wind_v);
        args.putString(ARGUMENT_STATE, state);
        */
        args.putBoolean(ARGUMENT_AVAILABLE, available);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        _temperature = getArguments().getInt(ARGUMENT_TEMPERATURE);
        _wind_direction = getArguments().getString(ARGUMENT_WIND_DIRECTION);
        _wind_velocity = getArguments().getString(ARGUMENT_WIND_VELOCITY);
        _state = getArguments().getString(ARGUMENT_STATE);
        */
        _available = getArguments().getBoolean(ARGUMENT_AVAILABLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, null);

        img = (ImageView) view.findViewById(R.id.weather_image);
        _temperature_tv = (TextView) view.findViewById(R.id.temperature);
        _state_tv = (TextView) view.findViewById(R.id.comment);
        _wind_velocity_tv = (TextView) view.findViewById(R.id.wind_velocity);
        _wind_direction_tv = (TextView) view.findViewById(R.id.wind_direction);

        return view;
    }

    public void refresh() {
        Internet.updateWeatherData("https://192.168.43.119/weather", this);
    }

    public void updateData(String temperature, final String wind_d, final String wind_v, final String comment) {
        //String text = temperature + getString(R.string.one_space) + getString(R.string.degree);
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

        /*switch (comment) {
            case "пасмурно":
                setImg(R.drawable.weather_overcast);
                break;
            case "небольшой дождь":
                setImg(R.drawable.weather_small_rain);
                break;
            case "облачно с прояснениями":
                setImg(R.drawable.weather_cloudy);
                break;
            default:
                setImg(R.drawable.weather_default);
        }*/

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

        //img.setImageBitmap(bitmap);

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



        /*

        _state_tv.setTextColor(color);

        _wind_direction_tv.setTextColor(color);

        _wind_velocity_tv.setTextColor(color);

        _temperature_tv.setTextColor(color);

        */
    }
}
