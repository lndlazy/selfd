package net.xnzn.app.selfdevice.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;

import androidx.annotation.NonNull;

public class MyTouchDialog extends AlertDialog {
    public MyTouchDialog(@NonNull Context context) {
        this(context, 0);
    }

    public MyTouchDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                resetTime();
                break;
        }

        return super.dispatchTouchEvent(ev);

    }

    protected void resetTime() {

    }


}