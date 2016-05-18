package com.joey.general.utils;

/**
 * 用于判断一串数字是否是手机号
 *
 * @author Administrator
 */
public class CheckPhoneNumber {

    /**
     * 移动号码
     */
    public static final int TYPE_CHINA_MOBILE = 1;
    /**
     * 联通号码
     */
    public static final int TYPE_CHINA_UNICOM = 2;
    /**
     * 电信号码
     */
    public static final int TYPE_CHINA_TELECOM = 3;
    /**
     * 未知类型
     */
    public static final int TYPE_UNKNOW = 4;
    /**
     * 号码错误
     */
    public static final int TYPE_ERROR = 5;
    /*
     * 移动: 2G号段(GSM网络)有139,138,137,136,135,134,159,158,152,151,150,
     * 3G号段(TD-SCDMA网络)有157,182,183,188,187 147,178是移动TD上网卡专用号段.联通:
     * 2G号段(GSM网络)有130,131,132,155,156 3G号段(WCDMA网络)有186,185,176 电信:
     * 2G号段(CDMA网络)有133,153 3G号段(CDMA网络)有189,180,177
     */
    static String YD = "^[1]{1}(([3]{1}[4-9]{1})|([5]{1}[012789]{1})|([8]{1}[2378]{1})|([7]{1}[8]{1})|([4]{1}[7]{1}))[0-9]{8}$";
    static String LT = "^[1]{1}(([3]{1}[0-2]{1})|([5]{1}[56]{1})|([7]{1}[6]{1})|([8]{1}[56]{1}))[0-9]{8}$";
    static String DX = "^[1]{1}(([3]{1}[3]{1})|([5]{1}[3]{1})|([7]{1}[7]{1})|([8]{1}[109]{1}))[0-9]{8}$";

    public static int matchNum(String mobPhnNum) {

        /**
         * flag = 1 YD 2 LT 3 DX
         */
        int flag;// 存储匹配结果

        for (int i = 0; i < mobPhnNum.length(); i++) {

            if (!Character.isDigit(mobPhnNum.charAt(i))) {
                flag = TYPE_UNKNOW;
                return flag;
            }
        }


        // 判断手机号码是否是11位
        if (mobPhnNum.length() == 11) {

//            // 判断手机号码是否符合中国移动的号码规则
//            if (mobPhnNum.matches(YD)) {
//                flag = TYPE_CHINA_MOBILE;
//            }
//            // 判断手机号码是否符合中国联通的号码规则
//            else if (mobPhnNum.matches(LT)) {
//                flag = TYPE_CHINA_UNICOM;
//            }
//            // 判断手机号码是否符合中国电信的号码规则
//            else if (mobPhnNum.matches(DX)) {
//                flag = TYPE_CHINA_TELECOM;
//            }
            // 判断手机号码是否符合中国移动的号码规则
            if (mobPhnNum.startsWith("1")) {
                flag = TYPE_CHINA_MOBILE;
            }
            // 都不合适 未知
            else {
                flag = TYPE_UNKNOW;
            }
        }
        // 不是11位
        else {
            flag = TYPE_ERROR;
        }
        return flag;
    }
}
