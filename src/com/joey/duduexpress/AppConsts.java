package com.joey.duduexpress;

import android.os.Environment;

import java.io.File;

public class AppConsts {

    /**
     * app info
     **/
    public static final String APP_NAME = "XiaoWei";
    public static final int APP_KEY = 1;// 1是小维，以后定制版本往后累加
    public static final boolean APP_CUSTOM = false;// 是否定制 true：定制 false：小维
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
    public static final String LOG_PATH_DISMISS = SD_CARD_PATH + APP_NAME
            + File.separator + ".log" + File.separator;
    public static String LOG_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + ".log" + File.separator;
    public static String LOG_CLOUD_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + ".logcloud" + File.separator;
    public static String LOG_ACCOUNT_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + ".logaccout" + File.separator;
    public static final String LOG_CLOUD_PATH_DISMISS = SD_CARD_PATH + APP_NAME
            + File.separator + ".logcloud" + File.separator;
    public static final String LOG_ACCOUNT_PATH_DISMISS = SD_CARD_PATH + APP_NAME
            + File.separator + ".logaccout" + File.separator;
    public static final String LOG_PATH_SHOW = SD_CARD_PATH + APP_NAME
            + File.separator + "log" + File.separator;
    public static final String LOG_CLOUD_PATH_SHOW = SD_CARD_PATH + APP_NAME
            + File.separator + "logcloud" + File.separator;
    public static final String LOG_ACCOUNT_PATH_SHOW = SD_CARD_PATH + APP_NAME
            + File.separator + "logaccout" + File.separator;
    // 小维不用此路径
    public static final String AD_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + "ad" + File.separator;
    public static final String CAPTURE_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + "capture" + File.separator;
    public static final String VIDEO_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + "video" + File.separator;
    public static final String DOWNLOAD_VIDEO_PATH = SD_CARD_PATH
            + APP_NAME + File.separator + "downvideo" + File.separator;
    public static final String BUG_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + "bugs" + File.separator;
    public static final String DOWNLOAD_IMAGE_PATH = SD_CARD_PATH
            + APP_NAME + File.separator + "downimage" + File.separator;
    public static final String HEAD_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + "head" + File.separator;
    public static final String WELCOME_IMG_PATH = SD_CARD_PATH
            + APP_NAME + File.separator + "welcome" + File.separator;
    public static final String SCENE_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + "scene" + File.separator;
    public static final String BBSIMG_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + "bbsimage" + File.separator;
    // 云存储边下载边播放ts,m3u8文件路径
    public static final String CLOUDVIDEO_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + "cloudvideo" + File.separator;
    // 报警图片路径
    public static final String ALARM_IMG_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + "alarmimage" + File.separator;
    // 报警视频路径
    public static final String ALARM_VIDEO_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + "alarmvideo" + File.separator;
    // 猫眼路径
    public static final String CAT_PATH = SD_CARD_PATH + APP_NAME
            + File.separator + "cat" + File.separator;
    /*************
     * 媒体类型
     ********************/
    public static final String IMAGE_PNG_KIND = ".png";// 图片类型
    public static final String IMAGE_JPG_KIND = ".jpg";// 图片类型
    public static final String VIDEO_MP4_KIND = ".mp4";// 视频类型
    public static final String VIDEO_M3U8_KIND = ".m3u8";// 视频类型
    public static final int RECORD_AMR = 0;// 音频格式
    public static final int RECORD_ALAW = 1;// 音频格式
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
