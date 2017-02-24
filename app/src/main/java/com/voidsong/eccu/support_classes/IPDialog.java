package com.voidsong.eccu.support_classes;

import android.app.AlertDialog;
import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.voidsong.eccu.R;

/**
 * Created by CoolHawk on 2/24/2017.
 */


    EditText edit;
    public IPDialog(Context context, AttributeSet atts) {
        super(context, atts);
        setDialogLayoutResource(R.layout.dialog_login);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);
    }

    @Override
    protected View onCreateDialogView() {
        this.edit = new EditText(this.getContext());
        return this.edit;
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            Settings.setIp(this.edit.getText().toString());
        }
        super.onDialogClosed(positiveResult);
    }

}

