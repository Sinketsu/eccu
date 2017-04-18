package com.voidsong.eccu.support_classes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.voidsong.eccu.FragmentCamera;
import com.voidsong.eccu.R;

public class CustomFragmentPagerAdapter extends FragmentPagerAdapter{

    public CustomFragmentPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FragmentCamera.new_instance(R.drawable.fon);
            case 1:
                return FragmentCamera.new_instance(R.drawable.logo);
            case 2:
                return FragmentCamera.new_instance(R.drawable.set);
            default:
                return FragmentCamera.new_instance(R.drawable.fon);
        }
    }

    @Override
    public int getCount() {
        return 4; // TODO change to 4
    }
}
