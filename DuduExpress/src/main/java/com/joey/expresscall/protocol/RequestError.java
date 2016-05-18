package com.joey.expresscall.protocol;

import android.content.res.Resources;

import com.joey.general.MainApplication;
import com.joey.expresscall.R;

public class RequestError {

	public static final int ERROR_CODE_NULL = 0;
    /**
     * 自己定义的错误码
     */
    // 未登录
    public static final int ERROR_CODE_NOT_LOGIN = 1000;
    /**
     * Jar包中定义的服务器错误码(使用登录回调, 报警开关设置回调中)
     */
    /*
     * Status code
     */
    // #define BIZ_ACC_STATUS_OK 0 /* No errors encountered */
    // #define BIZ_ACC_STATUS_BUSY 1 /* System busy */
    // #define BIZ_ACC_STATUS_CONNECT 2 /* Connect error */
    // #define BIZ_ACC_STATUS_MSG 3 /* Msg struct error */
    // #define BIZ_ACC_STATUS_RID 4 /* RID error */
    // #define BIZ_ACC_STATUS_SID 5 /* SID error */
    // #define BIZ_ACC_STATUS_IP 6 /* IP error */
    // #define BIZ_ACC_STATUS_AUTH 7 /* Auth error */ /* 鉴权失败 */
    // #define BIZ_ACC_STATUS_AUTH_USR 8 /* Auth usr error */ /* 用户名不存在 */
    // #define BIZ_ACC_STATUS_AUTH_PWD 9 /* Auth pwd error */ /* 密码错误 */
    // #define BIZ_ACC_STATUS_VERSION 10 /* Version error */
    // #define BIZ_ACC_STATUS_REDIRECT 11 /* REDIRECT error */
    // #define BIZ_ACC_STATUS_SYSERR 20 /* System error */
    // 服务端异常，返回的json 是null http请求失败
    public static final int ERROR_CODE_SERVER_ERROR = 1001;
    // 服务端返回数据格式错误
    public static final int ERROR_CODE_RESPONSE_FORMAT_ERROR = 1002;
    // 离线报警中没有获取到TOKEN(废弃_20151117)
    public static final int ERROR_CODE_TOEKN_ERROR = 1003;
    // 无网络连接(wifi/移动网络都不行)
    public static final int ERROR_CODE_NETWORK_ERROR_UNAVAILABLE = 1004;
    // 有网(wifi/移动网络),但是网不通
    public static final int ERROR_CODE_NETWORK_ERROR_NOT_USE = 1005;
    // DNS下载/解析失败
    public static final int ERROR_CODE_DNS = 1006;
    // 应用内部处理异常(可能是类型转换异常或者onSuccess中解析有误)
    public static final int ERROR_CODE_APP_ERROR = 1007;
    // 请求超时
    public static final int ERROR_CODE_CONNECT_TIMEOUT = 1008;
    // 响应超时
    public static final int ERROR_CODE_READ_TIMEOUT = 1009;
    // 从线程池取连接超时
    public static final int ERROR_CODE_CONNECTION_POOL_TIMEOUT = 1010;
    // 请求接口其它异常
    public static final int ERROR_CODE_WEB_INTERFACE = 1011;
    // 参数设置时异常
    public static final int ERROR_CODE_PARAMS = 1012;
    // 参数中对应的value值为null
    public static final int ERROR_CODE_VALUE = 1013;
    // 读取/解析接口返回数据异常
    public static final int ERROR_CODE_READ_PARSE = 1014;
    // 任务执行超时
    public static final int ERROR_CODE_TASK_TIMEOUT = 1015;
    // 账号异常
    public static final int ERROR_CODE_ACCOUNT_EXCEPTION = 1016;
    private static final String TAG = RequestError.class.getSimpleName();
    public int errcode;
    public String errmsg;
    private Resources mResources = MainApplication.getInstance().getContext().getResources();

    public RequestError(int code) {
        this.errcode = code;
        this.errmsg = getMsgFromCode(code);
    }

    public RequestError(int code, String msg) {
        this.errcode = code;
        this.errmsg = msg;
    }

    private String getMsgFromCode(int errCode) {
        String msg = "";
        switch (errCode) {
            // --------------------自定义错误码信息-------------------------
            case ERROR_CODE_NOT_LOGIN:
                msg = mResources.getString(R.string.error_customize_not_login);
                break;
            case ERROR_CODE_SERVER_ERROR:
                msg = mResources.getString(R.string.error_customize_server);
                break;
            case ERROR_CODE_RESPONSE_FORMAT_ERROR:
                msg = mResources.getString(R.string.error_customize_response_format);
                break;
            case ERROR_CODE_TOEKN_ERROR:
                msg = mResources.getString(R.string.error_customize_token_error);
                break;
            case ERROR_CODE_NETWORK_ERROR_UNAVAILABLE:
                msg = mResources.getString(R.string.error_customize_network_error_unavailable);
                break;
            case ERROR_CODE_NETWORK_ERROR_NOT_USE:
                msg = mResources.getString(R.string.error_customize_network_error_not_use);
                break;
            case ERROR_CODE_DNS:
                msg = mResources.getString(R.string.error_customize_dns_error);
                break;
            case ERROR_CODE_CONNECT_TIMEOUT:
                msg = mResources.getString(R.string.error_customize_connect_timeout);
                break;
            case ERROR_CODE_READ_TIMEOUT:
                msg = mResources.getString(R.string.error_customize_read_timeout);
                break;
            case ERROR_CODE_CONNECTION_POOL_TIMEOUT:
                msg = mResources.getString(R.string.error_customize_connectionpool_timeout);
                break;
            case ERROR_CODE_WEB_INTERFACE:
                msg = mResources.getString(R.string.error_customize_web_interface);
                break;
            case ERROR_CODE_PARAMS:
                msg = mResources.getString(R.string.error_customize_params);
                break;
            case ERROR_CODE_VALUE:
                msg = mResources.getString(R.string.error_customize_value);
                break;
            case ERROR_CODE_READ_PARSE:
                msg = mResources.getString(R.string.error_customize_read_parse);
                break;
            case ERROR_CODE_TASK_TIMEOUT:
                msg = mResources.getString(R.string.error_customize_task_timeout);
                break;
            case ERROR_CODE_ACCOUNT_EXCEPTION:
                msg = mResources.getString(R.string.error_customize_account_exception);
                break;
            default:
                msg = mResources.getString(R.string.error_unknown);
                break;
        }
        return msg;
    }

    @Override
    public String toString() {
        return "Error [errcode=" + errcode + ", errmsg=" + errmsg + "]";
    }

}
