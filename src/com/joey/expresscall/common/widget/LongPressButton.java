package com.joey.expresscall.common.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.joey.general.utils.MyLog;

/**
 * Created by Joey on 2016/5/18.
 */
public class LongPressButton extends Button {

    private Handler handler = new Handler();
    private boolean startLongCheck;
    private OnLongPressListener pressListener;
//    private Runnable checkLongClickRunnable = new Runnable() {
//        @Override
//        public void run() {
//            if (startLongCheck) {
//                if (isOnTouch) {
//                    handler.postDelayed(checkLongClickRunnable, 1000);
//                    return;
//                }
//                startLongCheck = false;
//            }
//            if (pressListener != null) {
//                pressListener.onFinishPress(LongPressButton.this);
//            }
//            handler.removeCallbacks(checkLongClickRunnable);
//
//        }
//    };

    private View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            MyLog.i("onLongClick ");
            startLongCheck = true;
            if (pressListener != null) {
                pressListener.onStartPress(v);
            }
            return true;
        }
    };

    private boolean isOnTouch = false;

    public LongPressButton(Context context) {
        super(context);
        init();
    }

    public LongPressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LongPressButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnLongClickListener(longClickListener);
    }

    public boolean isOnTouch() {
        return isOnTouch;
    }

    public void setOnLongPressListener(OnLongPressListener l){
        pressListener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        MyLog.i("");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isOnTouch = true;
                break;
            case MotionEvent.ACTION_UP:
                isOnTouch = false;
                if(startLongCheck) {
                    if(pressListener != null){
                        pressListener.onFinishPress(this);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);

    }

    public interface OnLongPressListener {
        void onStartPress(View view);

        void onFinishPress(View view);
    }
}
