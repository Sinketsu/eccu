package com.voidsong.eccu.support_classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.voidsong.eccu.FragmentCamera;
import com.voidsong.eccu.R;
import com.voidsong.eccu.abstract_classes.RefreshableFragment;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter{

    private RefreshableFragment[] fragments = new RefreshableFragment[4];

    public CustomFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        fragments[0] = FragmentCamera.new_instance(R.drawable.fon);
        fragments[1] = FragmentCamera.new_instance(R.drawable.set);
        fragments[2] = FragmentCamera.new_instance(R.drawable.logo);
        fragments[3] = FragmentCamera.new_instance(R.drawable.fon);
    }

    @Override
    public RefreshableFragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }
}
