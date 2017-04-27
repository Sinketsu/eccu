package com.voidsong.eccu;

import android.annotation.TargetApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.voidsong.eccu.abstract_classes.RefreshableFragment;
import com.voidsong.eccu.dialogs.BulbDialog;
import com.voidsong.eccu.dialogs.DoorDialog;
import com.voidsong.eccu.dialogs.InfoDialog;
import com.voidsong.eccu.support_classes.CustomFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity implements BulbDialog.IBulbController,
        DoorDialog.IDoorController, InfoDialog.IInfoController{

    ViewPager pager;
    CustomFragmentPagerAdapter pagerAdapter;
    RefreshableFragment fragment;
    TabLayout tabLayout;

    AppCompatButton infoButton;
    AppCompatButton refreshButton;
    AppCompatButton bulbButton;
    AppCompatButton doorButton;

    DoorDialog doorDialog;
    BulbDialog bulbDialog;
    InfoDialog infoDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // try to add animations
        setAnimations();

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

        doorDialog = new DoorDialog();
        bulbDialog = new BulbDialog();
        infoDialog = new InfoDialog();

        doorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                doorDialog.show(getSupportFragmentManager(), "DoorControl");
            }
        });

        bulbButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                bulbDialog.show(getSupportFragmentManager(), "BulbControl");
            }
        });

        infoButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v){
                infoDialog.show(getSupportFragmentManager(), "InfoControl");
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
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
    public void setOpenedDoorCount(Integer opened, Integer all) {
        String text = getResources().getString(R.string.door_label) +
                getResources().getString(R.string.double_space) +
                opened +
                getResources().getString(R.string.splitter) +
                all;
        doorButton.setText(text);
    }

    @Override
    public void setTemperature(Integer temperature) {
        String text = "" + temperature +
                getResources().getString(R.string.one_space) +
                getResources().getString(R.string.degree);
        infoButton.setText(text);
    }

    @TargetApi(21)
    private void setAnimations() {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setEnterTransition(new Explode());
    }
}
