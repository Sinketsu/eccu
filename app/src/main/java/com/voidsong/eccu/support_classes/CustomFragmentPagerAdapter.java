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
        /*
        fragments[0] = FragmentCamera.new_instance(R.drawable.fon, true);
        fragments[1] = FragmentCamera.new_instance(R.drawable.set, true);
        fragments[2] = FragmentCamera.new_instance(R.drawable.logo, true);
        fragments[3] = FragmentCamera.new_instance(R.drawable.fon, true);
        */
        titles[0] = "weather";
        titles[1] = "camera 1";
        titles[2] = "camera 2";
        titles[3] = "camera 3";
    }

    public RefreshableFragment getFragment(int position) {
        return fragments[position];
    }

    @Override
    public Fragment getItem(int position) {
        RefreshableFragment fragment;
        switch (position) {
            case 0:
                fragment = FragmentCamera.new_instance(R.drawable.fon, true);
                fragments[0] = fragment;
                return fragment;
            case 1:
                fragment = FragmentCamera.new_instance(R.drawable.set, true);
                fragments[0] = fragment;
                return fragment;
            case 2:
                fragment = FragmentCamera.new_instance(R.drawable.logo, false);
                fragments[0] = fragment;
                return fragment;
            case 3:
                fragment = FragmentCamera.new_instance(R.drawable.fon, false);
                fragments[0] = fragment;
                return fragment;
            default:
                fragment = FragmentCamera.new_instance(R.drawable.fon, true);
                fragments[0] = fragment;
                return fragment;
        }
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
