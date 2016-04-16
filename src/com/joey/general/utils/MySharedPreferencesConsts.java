package com.joey.general.utils;

/**
 * 往SharedPreferences中存储时的一些键值
 */
public class MySharedPreferencesConsts {

    // MTU配置
    public static final String PROFILE_SETTING_MTU_RADIO_BTN = "MtuRadioBtn";
    /**
     * 设置中心中的一些开关
     */
    // 2G/3G/4G 提醒
    public static final String CK_NET_REMIND_KEY = "ck_net_remind_key";
    // 应用是否接收消息提醒
    public static final String CK_MSG_PUSH_SWITCH_KEY = "ck_msg_push_switch_key";
    // 提醒方式(铃声)
    public static final String CK_TIPS_RING_KEY = "ck_tips_ring_key";
    // 提醒方式(震动)
    public static final String CK_TIPS_VIBRATOR_KEY = "ck_tips_vibrator_key";
    /**
     * 账号信息
     */
    // 用户信息
    public static final String USERINFO = "userinfo";
    // 用户名
    public static final String USERNAME = "username";
    // 密码
    public static final String PASSWORD = "password";
    /**
     * 推送信息
     */
    // 推送类型
    public static final String PUSH_TYPE = "push_type";
    // 推送消息
    public static final String PUSH_MSG = "push_msg";
    // 报警信息
    public static final String PUSH_TEXT = "push_text";
    // 设备分享
    public static final String PUSH_SHARE = "push_share";
    /**
     * 开机界面/广告等
     */
    // 开机界面图片的版本号
    public static final String SPLASH_VERSION = "splash_version";
    /**
     * 服务器连接成功/断开提示
     */
    public static final String SERVER_EVENT_TYPE = "server_event_type";
    public static final String SERVER_EVENT_MSG = "server_event_msg";
    // 离线报警服务返回的TOKEN(这个值在账号登陆的时候会用到)
    public static String TOKEN = "TOKEN";
    // 注销标志
    public static String LOGOUT_TAG = "LOGOUT_TAG";
    // 应用是否处理接收到的广播
    public static String HANDLE_BROADCAST = "handle_broadcast";
    // 应用是否处理接收到的服务器改变信息(服务器连接成功/断开)
    public static String HANDLE_SERVER_CHANGE = "handle_server_change";

}
