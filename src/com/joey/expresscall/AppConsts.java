package com.joey.expresscall;

import android.os.Environment;

import java.io.File;

public class AppConsts {

    /**
     * app info
     **/
    public static final String APP_NAME = "ExpressCall";
    public static final int APP_KEY = 1;// 1是小维，以后定制版本往后累加
    public static final boolean APP_CUSTOM = false;// 是否定制 true：定制 false：小维
    public static boolean DEBUG_STATE = true;
    /**
     * 广播
     **/
    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
//    public static final boolean ENABLE_LOG = true;//是否打印日志
    public static final String LOCALE_CHANGED_ACTION = "android.intent.action.LOCALE_CHANGED";
    /*************
     * 程序中用到的路径
     ********************/

  
    
    public static final String SD_CARD_PATH = Environment
            .getExternalStorageDirectory().getPath() + File.separator;
    public static final String APP_PATH = SD_CARD_PATH + APP_NAME + File.separator;
    public static String LOG_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + ".log" + File.separator;
    public static final String RECORD_DIR = APP_PATH + "Records"+ File.separator;
    public static final String FILE_DIR = APP_PATH + "Files"+File.separator;
    
    /**
     * 日期formater
     **/
    public static final String FORMATTER_DATE_AND_TIME0 = "MM/dd/yyyy HH:mm:ss";
    public static final String FORMATTER_DATE_AND_TIME1 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMATTER_DATE_AND_TIME2 = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMATTER_DATE_AND_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMATTER_DATE = "yyyy-MM-dd";
    public static final String FORMATTER_TIME = "HH-mm-ss";
    // SD卡总容量
    public static final String TAG_NTOTALSIZE = "nTotalSize";
    // SD卡剩余容量
    public static final String TAG_NUSEDSIZE = "nUsedSize";
    // SD卡状态 nStatus: 0:未发现SD卡 1：未格式化 2：存储卡已满 3：录像中... 4：准备就绪
    public static final String TAG_NSTATUS = "nStatus";
    // 移动侦测灵敏度
  
}
