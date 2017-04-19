package com.voidsong.eccu.support_classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.voidsong.eccu.FragmentCamera;
import com.voidsong.eccu.R;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter{

    private Fragment[] fragments = new Fragment[4];

    public CustomFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments[0] = FragmentCamera.new_instance(R.drawable.fon);
        fragments[1] = FragmentCamera.new_instance(R.drawable.set);
        fragments[2] = FragmentCamera.new_instance(R.drawable.logo);
        fragments[3] = FragmentCamera.new_instance(R.drawable.fon);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
