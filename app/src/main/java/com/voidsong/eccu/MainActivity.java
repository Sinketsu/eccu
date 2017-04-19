package com.voidsong.eccu;

import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.voidsong.eccu.interfaces.IRefreshable;
import com.voidsong.eccu.support_classes.CustomFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    CustomFragmentPagerAdapter pagerAdapter;
    IRefreshable fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
    }

    public void OnButtonClick(View v) {
        fragment = pagerAdapter.getItem(pager.getCurrentItem()); // TODO change
        fragment.refresh();
    }
}
