package com.joey.general;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap.CompressFormat;


import com.joey.expresscall.storage.JVDbHelper;
import com.joey.general.utils.MobileUtil;
import com.joey.general.utils.MySharedPreference;


import java.util.HashMap;

/**
 * 整个应用的入口，管理状态、活动集合，消息队列以及漏洞汇报
 *
 * @author neo
 */
public class MainApplication extends Application  {

    private static final String TAG = "MainApplication";
    private static MainApplication mAppInstance;
   
    private Context mAppContext;
    private HashMap<String, Object> statusHashMap;
    // 账号操作对象句柄

    /**
     * 获取context
     *
     * @return
     */
    public static MainApplication getInstance() {
        return mAppInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppInstance = this;
        mAppContext = getApplicationContext();
        // 获取当前进程名称
       init();
    }

    /**
     * 应用进程(com.jovision.xiaowei)的初始化操作
     */
    private void init() {

        statusHashMap = new HashMap<String, Object>();
        setupDefaults();
        // 数据库小助手
        JVDbHelper.getInstance().init(this);
        MySharedPreference.getInstance().init(this);
//        创建文件目录
        MobileUtil.creatAllFolder();
    }

    /**
     * 离线服务进程(com.jovision.xiaowei:message)
     */
    private void initOfflineService() {
    }

    /**
     * 默认配置
     */
    private void setupDefaults() {
        
    }

    /**
     * 获取状态 Map
     *
     * @return
     */
    public HashMap<String, Object> getStatusHashMap() {
        return statusHashMap;
    }
    
    /**
     * 获取context
     *
     * @return
     */
    public Context getContext() {
        return mAppContext;
    }


}
