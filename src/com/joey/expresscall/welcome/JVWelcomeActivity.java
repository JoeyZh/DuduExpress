package com.jovision.xiaowei.welcome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.test.AutoLoad;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jovision.AppConsts;
import com.jovision.JVSharedPreferencesConsts;
import com.jovision.service.OfflineAlarmService;
import com.jovision.xiaowei.BaseActivity;
import com.jovision.xiaowei.MainApplication;
import com.jovision.xiaowei.R;
import com.jovision.xiaowei.account.JVAccountManager;
import com.jovision.xiaowei.account.ResponseListener;
import com.jovision.xiaowei.bean.RequestError;
import com.jovision.xiaowei.login.JVLoginActivity;
import com.jovision.xiaowei.mydevice.JVMainActivity;
import com.jovision.xiaowei.utils.ComponentUtil;
import com.jovision.xiaowei.utils.ConfigUtil;
import com.jovision.xiaowei.utils.MobileUtil;
import com.jovision.xiaowei.utils.MyLog;
import com.jovision.xiaowei.utils.MySharedPreference;
import com.jovision.xiaowei.utils.NetWorkUtil;
import com.jovision.xiaowei.utils.ResourcesUnusualUtil;
import com.jovision.xiaowei.utils.ToastUtil;
import com.tencent.stat.StatService;
import com.xiaowei.comm.Account;

public class JVWelcomeActivity extends BaseActivity {
    private static final String TAG = "JVWelcomeActivity";

    static {
        AutoLoad.foo();
    }

    /**
     * 初始化SDK线程
     */
    Thread initSDKThread = new Thread() {

        @Override
        public void run() {
            super.run();
            boolean same = ConfigUtil.getCloudJniVersion();
            if (AppConsts.DEBUG_STATE) {
                if (!same) {
                    ToastUtil.show(JVWelcomeActivity.this, "播放库用错了，不要测试了，快去找研发！");
                }
            }
            int region = ConfigUtil.getSinaRegion();
            boolean initCloudSDK = ConfigUtil.initCloudSDK(JVWelcomeActivity.this);
        }

    };
    /**
     * 开始线程
     */
    Thread startThread = new Thread() {

        @Override
        public void run() {
            super.run();
            if (MySharedPreference.getBoolean(AppConsts.FIRST_OPEN_APP, true)) {
                Intent startIntent = new Intent(JVWelcomeActivity.this, JVGuidActivity.class);
                JVWelcomeActivity.this.startActivity(startIntent);
                JVWelcomeActivity.this.finish();
            } else {
                if (((MainApplication) getApplication()).isAutoLogin()) {
                    Intent intent = new Intent(JVWelcomeActivity.this,
                            JVMainActivity.class);
                    JVWelcomeActivity.this.startActivity(intent);
                    JVWelcomeActivity.this.finish();
                } else {
                    Intent startIntent = new Intent(JVWelcomeActivity.this, JVLoginActivity.class);
                    JVWelcomeActivity.this.startActivity(startIntent);
                    JVWelcomeActivity.this.finish();
                }
            }

        }

    };
    private Handler welcomeHandler;
    private ImageView welcomeImg;
    // 启动页图片
    private Bitmap mSplash;
    private String mSplashName;

    @Override
    public void onHandler(int what, int arg1, int arg2, Object obj) {

    }

    @Override
    public void onNotify(int what, int arg1, int arg2, Object obj) {

    }

    @Override
    protected void initSettings() {

        AppConsts.DEBUG_STATE = MySharedPreference.getBoolean("DEBUG_STATE", false);

        MobileUtil.creatAllFolder();
        AppConsts.CURRENT_LAN = ConfigUtil.getLanguage(this);

//        MyLog.init(AppConsts.LOG_PATH);
        MyLog.enableFile(AppConsts.DEBUG_STATE);
        MyLog.enableLogcat(AppConsts.DEBUG_STATE);

        initSDKThread.start();
        welcomeHandler = new Handler();
        welcomeHandler.postDelayed(startThread, 4 * 1000);
        // 检测离线推送服务是否在运行
        if (!ComponentUtil.isServiceWork("com.jovision.service.OfflineAlarmService")) {
            // 启动接收离线推送的服务
            Intent service = new Intent(this, OfflineAlarmService.class);
            startService(service);
        }

        // 提示网络类型
        boolean showNetTips = MySharedPreference.getBoolean(
                JVSharedPreferencesConsts.CK_NET_REMIND_KEY,
                true);
        if (showNetTips) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String netWorkMsg = NetWorkUtil.getSpecialNetWorkInfo();
                    if (!TextUtils.isEmpty(netWorkMsg)) {
                        ToastUtil.show(JVWelcomeActivity.this, netWorkMsg, Toast.LENGTH_LONG);
                    }
                }
            }, 100);
        }

        /** 获取启动页图片(图片名称:splash_当前语言对应的int值) */
        mSplashName = "splash_" + AppConsts.CURRENT_LAN;
        String splashPath = AppConsts.WELCOME_IMG_PATH + mSplashName
                + AppConsts.IMAGE_PNG_KIND;
        mSplash = BitmapFactory.decodeFile(splashPath);

        // 自动登录
        doAutoLogin();
    }

    @Override
    protected void initUi() {
        StatService.trackCustomEvent(this, "onCreate", "");
        setContentView(R.layout.welcome_layout);
        welcomeImg = (ImageView) findViewById(R.id.welcome_img);
        // 设置启动页图片
        if (mSplash != null) {
            welcomeImg.setImageBitmap(mSplash);
        } else {
            MyLog.v(TAG, "splash use default image!");
            // 使用默认的启动图片
            int defaultSplashRedId = ResourcesUnusualUtil.getDrawableID(mSplashName);
            welcomeImg.setImageResource(defaultSplashRedId);
        }
        if (AppConsts.DEBUG_STATE) {
            ToastUtil.show(JVWelcomeActivity.this, "无线ip=" + NetWorkUtil.getLocalHostIp() + ";账号库=" + Account.BIZ_ACC_VERSION_STRING);
        }

    }

    /**
     * 自动登录处理
     */
    private void doAutoLogin() {
        String userName = MySharedPreference
                .getString(JVSharedPreferencesConsts.USERNAME);
        String password = MySharedPreference
                .getString(JVSharedPreferencesConsts.PASSWORD);
        // 用户名和密码都不为空,就认为满足自动登录条件.
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            MyLog.v(TAG, "do auto login.");
            statusHashMap.put(JVSharedPreferencesConsts.USERNAME, userName);
            statusHashMap.put(JVSharedPreferencesConsts.PASSWORD, password);

            // 尝试登录
            JVAccountManager.getInstance().login(userName, password,
                    new ResponseListener<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject json) {
                            MyLog.v(TAG, "do auto login success.");
                        }

                        @Override
                        public void onError(RequestError error) {
                            MyLog.e(TAG, "do auto login error: errorCode:"
                                    + error.errcode + ", errorMsg:" + error.errmsg);
                        }
                    });
        }
    }

    @Override
    protected void saveSettings() {

    }

    @Override
    protected void freeMe() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getTitleLayout() {
        return -1;
    }
}
