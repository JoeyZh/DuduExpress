package com.joey.expresscall.welcome;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.joey.expresscall.AppConsts;
import com.joey.expresscall.R;
import com.joey.expresscall.account.ECAccountManager;
import com.joey.expresscall.login.JVLoginActivity;
import com.joey.expresscall.protocol.RequestError;
import com.joey.expresscall.protocol.ResponseListener;
import com.joey.general.BaseActivity;
import com.joey.general.utils.MobileUtil;
import com.joey.general.utils.MyLog;
import com.joey.general.utils.MySharedPreference;
import com.joey.general.utils.MySharedPreferencesConsts;
import com.joey.general.utils.NetWorkUtil;
import com.joey.general.utils.ResourcesUnusualUtil;
import com.joey.general.utils.ToastUtil;

public class JVWelcomeActivity extends BaseActivity {
    private static final String TAG = "JVWelcomeActivity";
    private ImageView welcomeImg;
    // 启动页图片
    private Bitmap mSplash;
    private String mSplashName;

    @Override
	public void initSettings() {
    	
        AppConsts.DEBUG_STATE = MySharedPreference.getInstance().getBoolean("DEBUG_STATE", false);

        MobileUtil.creatAllFolder();
        MyLog.enableFile(AppConsts.DEBUG_STATE);
        MyLog.enableLogcat(AppConsts.DEBUG_STATE);
        
        // 提示网络类型
        boolean showNetTips = MySharedPreference.getInstance().getBoolean(
                MySharedPreferencesConsts.CK_NET_REMIND_KEY,
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
//        /** 获取启动页图片(图片名称:splash_当前语言对应的int值) */
//        mSplashName = "splash_" + AppConsts.CURRENT_LAN;
//        String splashPath = AppConsts.WELCOME_IMG_PATH + mSplashName
//                + AppConsts.IMAGE_PNG_KIND;
//        mSplash = BitmapFactory.decodeFile(splashPath);

        // 自动登录
//        if(doAutoLogin()){
//        	return;
//        }
        handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
//				boolean firstOpen = MySharedPreference.getInstance().getBoolean(MySharedPreferencesConsts.FIRST_OPEN_APP);
				Intent intent = new Intent();
//				if(firstOpen){
					intent.setClass(JVWelcomeActivity.this,JVGuidActivity.class);
//				}else{
//					intent.setClass(JVWelcomeActivity.this,JVLoginActivity.class);
//				}
			    ToastUtil.show(JVWelcomeActivity.this, "跳转开始");
				startActivity(intent);
				finish();
			}
		}, 3000);
    }

    @Override
	public void initUi() {
        setContentView(R.layout.welcome_layout);
        welcomeImg = (ImageView) findViewById(R.id.welcome_img);
        // 设置启动页图片
//        if (mSplash != null) {
//            welcomeImg.setImageBitmap(mSplash);
//        } else {
//            MyLog.v(TAG, "splash use default image!");
//            // 使用默认的启动图片
//            int defaultSplashRedId = ResourcesUnusualUtil.getDrawableID(mSplashName);
//            welcomeImg.setImageResource(defaultSplashRedId);
//        }
        if (AppConsts.DEBUG_STATE) {
            ToastUtil.show(JVWelcomeActivity.this, "无线ip=" + NetWorkUtil.getLocalHostIp());
        }

    }

    /**
     * 自动登录处理
     */
    private boolean doAutoLogin() {
        String userName = MySharedPreference.getInstance()
                .getString(MySharedPreferencesConsts.USERNAME);
        String password = MySharedPreference.getInstance()
                .getString(MySharedPreferencesConsts.PASSWORD);
        // 用户名和密码都不为空,就认为满足自动登录条件.
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            MyLog.v(TAG, "do auto login.");
            statusHashMap.put(MySharedPreferencesConsts.USERNAME, userName);
            statusHashMap.put(MySharedPreferencesConsts.PASSWORD, password);

            // 尝试登录
            ECAccountManager.getInstance().login(userName, password,
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
            return true;
        }
        return false;
    }

    @Override
	public void saveSettings() {

    }

    @Override
	public void freeMe() {

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
