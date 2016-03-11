package com.joey.general;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;


import com.joey.duduexpress.storage.JVDbHelper;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import java.util.HashMap;

/**
 * 整个应用的入口，管理状态、活动集合，消息队列以及漏洞汇报
 *
 * @author neo
 */
public class MainApplication extends Application  {

    private static final String TAG = "MainApplication";
    private static final String PROCESS_MAIN_NAME = "com.jovision.xiaowei";
    private static final String PROCESS_SERVICE_NAME = "com.jovision.xiaowei:message";
    private static MainApplication mAppInstance;
    // 二级缓存需要的变量(现在没用到,Volley本身支持二级缓存)
    private static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
    private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
    private static int DISK_IMAGECACHE_QUALITY = 100;
    private Context mAppContext;
    private HashMap<String, String> statusHashMap;
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
       
    }

    /**
     * 应用进程(com.jovision.xiaowei)的初始化操作
     */
    private void init() {

        statusHashMap = new HashMap<String, String>();
        setupDefaults();
        // 数据库小助手
        JVDbHelper.getInstance().init(this);
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
     * 获取context
     *
     * @return
     */
    public Context getContext() {
        return mAppContext;
    }


}
