package com.voidsong.eccu.abstract_classes;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

public abstract class RefreshableFragment extends Fragment {
    public abstract void refresh();
    public abstract boolean is_available();
}
