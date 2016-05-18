package com.joey.general.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

/**
 * ToastUtils 工具类
 */
public class ToastUtil {

    private static Handler handler = new Handler(Looper.getMainLooper());
    private static Toast toast = null;
    private static Object synObj = new Object();

    private ToastUtil() {
        throw new AssertionError();
    }

    public static void show(Context context, int resId) {
        showMessage(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        showMessage(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        showMessage(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text, int duration) {
        showMessage(context, text, duration);
    }

    public static void show(Context context, int resId, Object... args) {
        showMessage(context, String.format(context.getResources().getString(resId), args),
                Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String format, Object... args) {
        showMessage(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        showMessage(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        showMessage(context, String.format(format, args), duration);
    }

    private static void showMessage(final Context context, final CharSequence text,
                                    final int duration) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                synchronized (synObj) {
                    if (toast != null) {
                        toast.setText(text);
                        toast.setDuration(duration);
                    } else {
                        toast = Toast.makeText(context, text, duration);
                    }
                    toast.setGravity(Gravity.CENTER, 0, 10);
                    toast.show();
                }
            }
        });
    }

    public static void cancel() {
        handler.removeCallbacksAndMessages(null);
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }

}
