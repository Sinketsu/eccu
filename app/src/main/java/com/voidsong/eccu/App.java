package com.voidsong.eccu;

import android.app.Application;

import com.bettervectordrawable.Convention;
import com.bettervectordrawable.VectorDrawableCompat;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        int[] ids = VectorDrawableCompat.findVectorResourceIdsByConvention(getResources(),
                R.drawable.class, Convention.RESOURCE_NAME_HAS_VECTOR_PREFIX);
        VectorDrawableCompat.enableResourceInterceptionFor(getResources(), ids);
    }
}