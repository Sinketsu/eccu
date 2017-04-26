package com.voidsong.eccu;

import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;

import com.voidsong.eccu.abstract_classes.RefreshableFragment;
import com.voidsong.eccu.interfaces.IController;
import com.voidsong.eccu.support_classes.CustomFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity implements IController{

    ViewPager pager;
    CustomFragmentPagerAdapter pagerAdapter;
    RefreshableFragment fragment;
    TabLayout tabLayout;

    AppCompatButton infoButton;
    AppCompatButton refreshButton;
    AppCompatButton bulbButton;
    AppCompatButton doorButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(4);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);

        infoButton = (AppCompatButton) findViewById(R.id.info);
        refreshButton = (AppCompatButton) findViewById(R.id.refresh);
        bulbButton = (AppCompatButton) findViewById(R.id.bulb);
        doorButton = (AppCompatButton) findViewById(R.id.door);

        doorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DialogFragment dialogFragment = new BulbDialog();
                dialogFragment.show(getSupportFragmentManager(), "BulbControl");
            }
        });

        bulbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAGMYTAG", "mi tuta");
                RefreshableFragment fragment = pagerAdapter.getFragment(pager.getCurrentItem());
                fragment.refresh();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // ignore :)
    }

    @Override
    public void setActiveBulbCount(Integer active, Integer all) {
        String text = getResources().getString(R.string.bulb_label) +
                getResources().getString(R.string.double_space) +
                active +
                getResources().getString(R.string.splitter) +
                all;
        bulbButton.setText(text);
    }

    @Override
    public void setActiveDoorCount(Integer active, Integer all) {
        String text = getResources().getString(R.string.door_label) +
                getResources().getString(R.string.double_space) +
                active +
                getResources().getString(R.string.splitter) +
                all;
        doorButton.setText(text);
    }




}
