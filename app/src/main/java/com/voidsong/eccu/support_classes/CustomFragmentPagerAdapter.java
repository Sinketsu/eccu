package com.voidsong.eccu.support_classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.voidsong.eccu.FragmentCamera;
import com.voidsong.eccu.R;
import com.voidsong.eccu.abstract_classes.RefreshableFragment;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter{

    private RefreshableFragment[] fragments = new RefreshableFragment[4];
    private String[] titles = new String[4];

    public CustomFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments[0] = FragmentCamera.new_instance(R.drawable.fon);
        fragments[1] = FragmentCamera.new_instance(R.drawable.set);
        fragments[2] = FragmentCamera.new_instance(R.drawable.logo);
        fragments[3] = FragmentCamera.new_instance(R.drawable.fon);

        titles[0] = "weather";
        titles[1] = "camera 1";
        titles[2] = "camera 2";
        titles[3] = "camera 3";
    }

    @Override
    public RefreshableFragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
