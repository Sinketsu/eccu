package com.voidsong.eccu.abstract_classes;

import android.widget.LinearLayout;

import com.github.zagum.switchicon.SwitchIconView;

public abstract class SmartControl {
    protected int _id;
    protected String _path_get;
    protected String _path_set;
    protected boolean _state;
    protected SwitchIconView icon;

    abstract public void setControl(LinearLayout control,
                                    SwitchIconView icon,
                                    final String url_get,
                                    final String url_set);
    abstract public void verifyState();
    public void set_state(final boolean state) {
        icon.post(new Runnable() {
                    @Override
                    public void run() {
                        icon.setIconEnabled(state);
                    }
                });
        _state = state;
    }
    public boolean get_state() {
        return _state;
    }
}
