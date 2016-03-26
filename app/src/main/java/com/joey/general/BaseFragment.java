package com.joey.general;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.joey.expresscall.R;
import com.joey.general.utils.MyLog;
import com.joey.general.views.TopBarLayout;


/**
 * Fragment的基类
 */
public abstract class BaseFragment extends Fragment  {
    // 日志标记
    protected String TAG = "";
    protected BaseActivity mActivity;
    protected FragHandler fragHandler = new FragHandler(this);
    /**
     * 标题
     */
    private View mTopBarView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // 设置日志标志
//        TAG = AppConsts.LOG_PREFIX_FRAGMENT + this.getClass().getSimpleName();
        TAG =  this.getClass().getSimpleName();
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_fragment_base_view, null);
        LinearLayout contentContainer = (LinearLayout) view.findViewById(R.id.xiaowei_root_view);

        // 添加标题栏
        if (getTitleLayout() != -1) {
            mTopBarView = inflater.inflate(getTitleLayout(), null);
            contentContainer.addView(mTopBarView,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
        }

        // 添加Fragment内容
        View contentView = createView(inflater, view, savedInstanceState);
        contentView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        contentContainer.addView(contentView);

        return view;
    }

    /**
     * 使用共通标题栏的Fragment必需要实现这个方法
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected View createView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (BaseActivity) getActivity();
//        if (null == mActivity
//                | false == mActivity instanceof IHandlerLikeNotify) {
//            throw new ClassCastException(
//                    "mActivity must an IHandlerLikeNotify impl");
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * 每一个界面展示的时候,都要判断是否有服务器的信息<br/>
         * 如果有服务器连接异常的信息,需要显示错误标题栏.正常,隐藏<br/>
         */
//
// interceptServerState(eventType, eventMsg);
    }

    /**
     * 子类重载该方法自定义标题布局文件<br />
     * 子类重载该方法返回-1,不显示标题
     *
     * @return
     */
    protected int getTitleLayout() {
        return R.layout.common_titlebar_view;
    }

    // ----------------------------------------------------------
    // Title bar
    // ----------------------------------------------------------

    /**
     * 获取TopBar
     *
     * @return
     */
    protected TopBarLayout getTopBarView() {
        if (mTopBarView instanceof TopBarLayout) {
            return (TopBarLayout) mTopBarView;
        }
        return null;
    }

    /**
     * @param visiable
     */
    protected void setTopBarVisiable(int visiable) {
        if (mTopBarView == null) {
            return;
        }
        if (visiable == View.VISIBLE) {
            showTitleView();
            return;
        }
        hideTitleView();
    }

    /**
     * 判断TopBar是否显示
     *
     * @return
     */
    protected final boolean isTitleShowing() {
        MyLog.d(TAG, "isTitleShowing hasTitle :" + (mTopBarView != null ? true : false));
        if (mTopBarView == null) {
            return false;
        }

        return mTopBarView.getVisibility() == View.VISIBLE;
    }

    /**
     * 隐藏TopBar
     */
    private final void hideTitleView() {
        MyLog.d(TAG, "hideTitleView hasTitle :" + (mTopBarView != null ? true : false));
        if (mTopBarView != null) {
            mTopBarView.setVisibility(View.GONE);
        }
    }

    /**
     * 显示TopBar
     */
    private final void showTitleView() {
        MyLog.d(TAG, "showTitleView hasTitle :" + (mTopBarView != null ? true : false));
        if (mTopBarView != null) {
            mTopBarView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 接收事件(异步接收,转化成同步)
     *
     * @param eventTag
     * @param obj
     */
    public void handleEventMainThread(int eventTag, Object obj) {
        Message msg = fragHandler.obtainMessage(eventTag);
        msg.obj = obj;
        fragHandler.sendMessage(msg);
    }

    // ------------------------------------------------------
    // ## 拦截处理(服务器连接成功/断开拦截)
    // ------------------------------------------------------

    /**
     * 异步转同步完成后,重新分发事件进行处理
     *
     * @param eventTag
     * @param obj
     */
    public void interceptDispatcher(int eventTag, Object obj) {
        Bundle bundle = (Bundle) obj;
        switch (eventTag) {
//            case SelfConsts.WHAT_INTERCEPT_SERVER:
//                interceptServerState(bundle.getInt("type"), bundle.getString("msg"));
//                break;
            default:
                break;
        }
    }

    /**
     * 拦截服务器的状态<br>
     * 比如:服务器链接成功/服务器链接断开等
     */
    public void interceptServerState(final int type, final String msg) {
        // 2015.12.07 规定只在设备列表界面显示通知
        // 2015/12/07 modify start
        // // 检测当前界面标题栏是否存在
        // if (!isTitleShowing()) {
        // return;
        // }
        // // 检测当前界面是否要显示服务器状态
        // String simpleName = this.getClass().getSimpleName();
        // String[] noServerState = getResources()
        // .getStringArray(R.array.array_no_server_status_filter);
        // boolean isNotShowServerState = false;
        // int len = noServerState.length;
        // for (int i = 0; i < len; i++) {
        // if (simpleName.equals(noServerState[i])) {
        // isNotShowServerState = true;
        // break;
        // }
        // }
        // if (isNotShowServerState) {
        // return;
        // }
        String simpleName = this.getClass().getSimpleName();
        if (!simpleName.equals("JVMyDeviceFragment")) {
            return;
        }
        // 2015/12/07 modify end

        TopBarLayout topBarView = getTopBarView();
//        switch (type) {
//            case Account.BIZ_ACC_EVENT_CONNECTED:// 服务器链接成功
//                topBarView.dismissAlarmNet();
//                break;
//            case Account.BIZ_ACC_EVENT_DISCONNECT:// 服务器链接断开
//            case Account.BIZ_ACC_EVENT_CONNECT_FAILED:// 连接服务器失败
//            case Account.BIZ_ACC_EVENT_CONNECT_REFRESH:// 刷新服务器地址
//                if (NetWorkUtil.IsNetWorkEnable()) {
//                    int loginStatusCode = JVAccountManager.getInstance().getLoginStatusCode();
//                    if (loginStatusCode == Account.BIZ_ACC_STATUS_AUTH
//                            || loginStatusCode == Account.BIZ_ACC_STATUS_AUTH_USR
//                            || loginStatusCode == Account.BIZ_ACC_STATUS_AUTH_PWD) {
//                        // 账号登录异常的情况(鉴权失败,用户名不存在,密码错误)
//                        topBarView.showAlarmNet(R.string.auto_login_account_exception,
//                                TopBarLayout.SUB_TITLEBAR_JUMP_LOGIN, null);
//                    } else {
//                        topBarView.showAlarmNet(R.string.server_abnormal, -1, null);
//                    }
//                } else {
//                    topBarView.showAlarmNet(R.string.net_disconnected,
//                            TopBarLayout.SUB_TITLEBAR_OPEN_NET, null);
//                }
//                break;
//            default:
//                break;
//        }
    }

    /**
     * login登录成功以后的回调
     */
    public void onLoginSuccess() {
    }

    protected class FragHandler extends Handler {
        private BaseFragment fragment;

        public FragHandler(BaseFragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void handleMessage(Message msg) {
            if (null != msg) {
                if (msg.what < 0x9000) {
//                    fragment.fragNotify.onHandler(msg.what, msg.arg1, msg.arg2,
//                            msg.obj);
                } else {
                    // 服务器状态的拦截事件
                    interceptDispatcher(msg.what, msg.obj);
                }
            }
            super.handleMessage(msg);
        }
    }
}
