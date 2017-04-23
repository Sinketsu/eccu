package com.voidsong.eccu;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.voidsong.eccu.abstract_classes.RefreshableFragment;
import com.voidsong.eccu.support_classes.CustomFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    ViewPager pager;
    CustomFragmentPagerAdapter pagerAdapter;
    RefreshableFragment fragment;
    TabLayout tabLayout;

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
    }
}
