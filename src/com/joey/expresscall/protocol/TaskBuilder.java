package com.joey.expresscall.protocol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.login.JVLoginActivity;
import com.joey.general.BaseActivity;
import com.joey.general.MyActivityManager;
import com.joey.general.utils.DateUtil;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.MySharedPreference;
import com.joey.general.utils.MySharedPreferencesConsts;
import com.joey.general.utils.ToastUtil;

import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * 任务,每调用web接口都算作一个任务<br/>
 */
public class TaskBuilder extends CommonHandler implements Callable<JVTask> {

    private final String TAG = this.getClass().getSimpleName();
    private OnTaskListener mTaskListener;
    private ResponseListener<?> mResponseListener;
    // 任务名称
    private String mTaskName;
    // 任务执行的超时时间(默认设置为1分钟)
    private int mTimeout = 60 * 1000;

    private JVTask mTask;
    // 任务开始/结束时间
    private long mTaskStartTime, mTaskEndTime;
   
    // 超时计算任务
    private SimpleTask mTimeoutTask = new SimpleTask() {
        @Override
        public void doInBackground() {
        }

        @Override
        public void onFinish(boolean canceled) {
            if (!canceled) {
                doTimeout();
            }
        }
    };

    public <T> TaskBuilder(String taskName, ResponseListener<T> rListener, OnTaskListener tListener) {
        mTaskName = taskName;
        mTaskListener = tListener;
        mResponseListener = rListener;
        // 创建一个存储任务信息的对象
        mTask = new JVTask();
        mTask.setTaskName(mTaskName);
    }

	public String getTaskName() {
        return mTaskName;
    }

    public void setTaskName(String name) {
        mTaskName = name;
    }
    

    @Override
    public JVTask call() throws Exception {
        // 开始超时计时
        beginTimeout();
        mTaskStartTime = System.currentTimeMillis();
        mTask.setTaskStartTime(DateUtil.getInternalTime());
        // ----------------------------------------------
            MyLog.v(TAG, "[" + mTaskName + "]" + "check login after, task execute.");
            /** 4.执行任务 */
            String jsonData = mTaskListener.execute();
            /** 5.任务执行完成,回调 */
            String result = handlerOperation(jsonData, mResponseListener);
            setTaskResult(result);
            // 如果session超时(回避策)
            if (result.indexOf("403") != -1) {
                MyLog.e(TAG, "[" + mTaskName + "]" + "session timeout, jump login page.");
                /** 6.session超时, 跳转到登录界面 */
                jumpLogin(R.string.login_expire);
            }

        return mTask;
    }


    /**
     * 处理返回结果<br/>
     * 应该只有web接口调用这个方法
     *
     * @param <T>
     * @param json
     */
    private <T> String handlerOperation(String json, ResponseListener<T> listener) {
        return new RequestHandler<T>(json, listener).execute(mTaskName);
    }

    /**
     * 跳转到登录
     */
    private void jumpLogin(int resId) {
       
        // 注销
//        ECAccountManager.getInstance().logout();
        // 获取当前Activity
        BaseActivity mActivity = (BaseActivity) MyActivityManager
                .getActivityManager().currentActivity();
        // 弹出提示
        resId = resId == -1 ? R.string.not_loginin : resId;
        ToastUtil.show(mActivity, resId);
        // 跳转到登录界面
        Intent intent = new Intent(mActivity, JVLoginActivity.class);
        intent.putExtra("isGoBack", true);
        mActivity.startActivity(intent);
//        mActivity.startActivityForResult(intent, BaseActivity.REQUEST_LOGIN);
    }

    /**
     * 设置任务结束的一些信息
     */
    private void setTaskResult(String result) {
        // 结束超时计时
        endTimeout();

        if (mResponseListener != null) {
            mResponseListener = null;
        }
        MyLog.v(TAG, "[" + mTaskName + "]" + "result:" + result);
        mTaskEndTime = System.currentTimeMillis();
        mTask.setTaskEndTime(DateUtil.getInternalTime());
        mTask.setResult(result);
        mTask.setTaskTime(calcTime());
        // 从任务列表中移除任务
        BackgroundHandler.removeTask(mTaskName);
    }

    /**
     * 任务耗时
     *
     * @return
     */
    private String calcTime() {
        long between = mTaskEndTime - mTaskStartTime;
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                - min * 60 * 1000 - s * 1000);
        String time = "" + day + "天" + hour + "小时" + min + "分" + s + "秒" + ms + "毫秒";
        return time;
    }

    /**
     * 开始计时
     */
    private void beginTimeout() {
        if (mTimeout > 0) {
            SimpleTask.postDelay(mTimeoutTask, mTimeout);
        }
    }

    /**
     * 结束计时
     */
    public void endTimeout() {
        mTimeoutTask.cancel();
    }

    /**
     * 任务超时后的一些操作
     */
    private void doTimeout() {
        MyLog.v(TAG, "[" + mTaskName + "]" + "execute timeout");
        // 取消任务
        BackgroundHandler.cancelTask(mTaskName);
        if (mResponseListener != null) {
            mResponseListener.onError(new RequestError(
                    RequestError.ERROR_CODE_TASK_TIMEOUT));
            mResponseListener = null;
        }
        setTaskResult("(-7)任务执行超时");
    }

    /**
     * 创建提示对话框
     *
     * @param resId 提示信息资源ID
     */
    private void createTipDialog(final int resId) {
        createTipDialog(resId, -1, -1);
    }

    /**
     * 创建提示对话框
     *
     * @param resId         提示信息资源ID
     * @param positiveEvent 点击确定后的动作
     * @param negativeEvent 点击取消后的动作
     */
    private void createTipDialog(final int resId, final int positiveEvent, final int negativeEvent) {
        // 获取当前Activity
        final BaseActivity mActivity = (BaseActivity) MyActivityManager
                .getActivityManager().currentActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
//        builder.setViewStyle(CustomDialog.DIALOG_STYLE_CRY);
        builder.setMessage(resId);
        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                switch (positiveEvent) {
                    case -1:// 默认操作(现在是跳转到登录)
                        jumpLogin(-1);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 关闭当前Activity
                mActivity.finish();
            }
        });

        if (!mActivity.isFinishing()) {
            builder.create().show();
        }
    }

    /**
     * 任务监听器
     */
    public interface OnTaskListener {
        /**
         * 任务具体执行
         *
         * @return 任务执行后的结果
         */
        public String execute();
    }
}
