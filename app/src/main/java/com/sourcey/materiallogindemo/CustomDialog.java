package com.sourcey.materiallogindemo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.NumberPicker;

/**
 * Created by CoderGuru on 15-08-2017.
 */

public class CustomDialog extends Dialog {
    public CustomDialog(Context context) {
        super(context, R.style.AppTheme_Dark_Dialog); //use your style id from styles.xml
    }

    public void setNumberDialog() {
        setContentView(R.layout.number_picker_dialog);
        NumberPicker minPicker = (NumberPicker) findViewById(R.id.hour_picker);
        minPicker.setMaxValue(24);
        minPicker.setMinValue(0);
        NumberPicker maxPicker = (NumberPicker) findViewById(R.id.min_picker);
        maxPicker.setMaxValue(60);
        maxPicker.setMinValue(0);
        //setTitle("Text Size");
        show();
    }
}
