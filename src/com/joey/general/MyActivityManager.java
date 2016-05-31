package com.joey.general;

import android.app.Activity;

import java.util.Stack;

public class MyActivityManager {
    public static Stack<Activity> activityStack;
    private static MyActivityManager instance;

    private MyActivityManager() {
    }

    public static MyActivityManager getActivityManager() {
        if (instance == null) {
            instance = new MyActivityManager();
        }
        return instance;
    }

    // 退出栈顶Activity
    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    // 获得当前栈顶Activity
    public Activity currentActivity() {
        Activity activity = null;
        if (null != activityStack && 0 != activityStack.size()) {
            activity = activityStack.lastElement();
        }
        return activity;
    }

    // 将当前Activity推入栈中
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    // 除了某个Activity,退出栈中其它所有Activity
    public void popAllActivityExceptOne(Class<?> cls) {
        if (null == activityStack || activityStack.size() == 0) {
            return;
        }
        int len = activityStack.size();
        for (int i = len - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (activity == null || (null != cls && activity.getClass().equals(cls))) {
                continue;
            }
            popActivity(activity);
        }
    }

    public boolean cleanStack(){
        if (null == activityStack || activityStack.size() == 0) {
            return false;
        }
        int len = activityStack.size();
        for (int i = len - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            popActivity(activity);
        }
        return true;
    }

    // 退出栈中当前Activity
    public void popThisActivity(Class<?> cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (null != cls && activity.getClass().equals(cls)) {
                popActivity(activity);
                break;
            }
        }
    }
}
