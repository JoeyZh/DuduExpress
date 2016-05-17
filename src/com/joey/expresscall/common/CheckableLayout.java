package com.joey.expresscall.common;

import com.joey.general.utils.MyLog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class CheckableLayout extends LinearLayout implements Checkable {

    private boolean isChecked = false;

    public CheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CheckableLayout(Context context) {
        super(context);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        MyLog.fmt("checked %s", checked + "");
        isChecked = checked;
        for (int i = 0, len = getChildCount(); i < len; i++) {
            View child = getChildAt(i);
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(checked);
            }
        }
    }

    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (int i = 0, len = getChildCount(); i < len; i++) {
            View child = getChildAt(i);
            if (child instanceof Checkable) {
                child.setEnabled(enabled);
            }
        }
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        for (int i = 0, len = getChildCount(); i < len; i++) {
            View child = getChildAt(i);
            if (child instanceof CheckBox) {
                ((CheckBox) child).setOnCheckedChangeListener(listener);
            }
        }
    }
}
