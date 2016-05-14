package com.joey.expresscall.protocol;

import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.login.JVLoginActivity;
import com.joey.general.BaseActivity;
import com.joey.general.MyActivityManager;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.MySharedPreference;
import com.joey.general.utils.NetWorkUtil;
import com.joey.general.utils.ToastUtil;

import android.content.Intent;

/**
 * 共通处理<br/>
 * 例:网络检测拦截,登录检测拦截等
 */
public class CommonHandler {
    private final String TAG = "CommonHandler";

    /**
     * 网络拦截
     */
    public <T> boolean interceptNetBase(ResponseListener<T> listener) {
        // 获取当前Activity
        BaseActivity mActivity = (BaseActivity) MyActivityManager
                .getActivityManager().currentActivity();

        boolean isEnable = NetWorkUtil.IsNetWorkEnable();

        if (!isEnable) {
            MyLog.e(TAG, "network off.");
            ToastUtil.show(mActivity, R.string.error_customize_network_error_unavailable);
            if (listener != null) {
                listener.onError(new RequestError(
                        RequestError.ERROR_CODE_NETWORK_ERROR_UNAVAILABLE));
            }
        }

        return isEnable;
    }

    /**
     * 网络拦截
     *
     * @param <T>
     */
    public <T> boolean interceptNetByServer(ResponseListener<T> listener) {

        boolean isEnable = interceptNetBase(listener);

        if (isEnable) {
            // 检测服务器推送过来的连接状态
           
        }

        return isEnable;
    }

    /**
     * 网络拦截(根据ping百度或者HttpUrlConnection连接百度的结果)
     */
    public <T> boolean interceptNetOther(ResponseListener<T> listener) {
        boolean isEnable = interceptNetBase(listener);

        if (isEnable) {
            // 检测是否能上网
            isEnable = NetWorkUtil.IsNetWorkConnected();

            if (!isEnable) {
                MyLog.e(TAG, "network on, ping or connection baidu failed.");
                // 获取当前Activity
                BaseActivity mActivity = (BaseActivity) MyActivityManager
                        .getActivityManager().currentActivity();
                ToastUtil.show(mActivity, R.string.error_customize_network_error_not_use);
                if (listener != null) {
                    listener.onError(new RequestError(
                            RequestError.ERROR_CODE_NETWORK_ERROR_NOT_USE));
                }
            }
        }

        return isEnable;
    }

    /**
     * 登录拦截(只有本地接口调用使用)
     *
     * @return
     */
    public boolean interceptLogin() {
        boolean isLogin = ECAccountManager.getInstance().isLogin();
        if (!isLogin) {
            MyLog.e(TAG, "not login, redirect login page!");
           
            // 获取当前Activity
            BaseActivity mActivity = (BaseActivity) MyActivityManager
                    .getActivityManager().currentActivity();
            MyActivityManager.getActivityManager().popAllActivityExceptOne(
                    mActivity.getClass());
            // 跳转到登录界面
            Intent intent = new Intent(mActivity, JVLoginActivity.class);
            mActivity.startActivity(intent);
            mActivity.finish();

            return false;
        } else {
            return true;
        }
    }

}
