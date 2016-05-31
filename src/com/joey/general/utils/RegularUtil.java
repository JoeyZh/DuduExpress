package com.joey.general.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularUtil {

    /**
     * 检查ip地址格式是否正确
     *
     * @param ipAddress
     * @return
     * @author
     */
    public static boolean checkIPAdress(String ipAddress) {
        if (ipAddress
                .matches("^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查端口号格式是否正确
     *
     * @param portNum
     * @return
     * @author suifupeng
     */
    public static boolean checkPortNum(String portNum) {
        if (portNum.length() < 1 || portNum.length() > 5) {
            return false;
        }
        for (int i = 0; i < portNum.length(); i++) {
            char c = portNum.charAt(i);
            if (c > '9' || c < '0') {
                return false;
            }
        }
        if (Integer.valueOf(portNum).intValue() <= 0
                && Integer.valueOf(portNum).intValue() > 65535) {
            return false;
        }
        return true;
    }


    // 验证设备昵称
    public static boolean checkDevNickName(String str) {
        boolean flag = false;
        try {
            byte[] b = str.getBytes("UTF-8");
            str = new String(b, "UTF-8");
            Pattern pattern = Pattern
                    .compile("^[A-Za-z0-9_.()\\+\\-\\u4e00-\\u9fa5]{0,20}$");

            Matcher matcher = pattern.matcher(str);
            if (matcher.matches() && 20 >= str.getBytes().length) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (UnsupportedEncodingException e) {
            flag = false;
        }
        return flag;
    }


    // 验证第三方设备昵称
    public static boolean checkThirdDevNickName(String str) {
        boolean flag = false;
        try {
            byte[] b = str.getBytes("UTF-8");
            str = new String(b, "UTF-8");
            Pattern pattern = Pattern
                    .compile("^[A-Za-z0-9_.()\\+\\-\\u4e00-\\u9fa5]{1,20}$");

            Matcher matcher = pattern.matcher(str);
            if (matcher.matches() && 20 >= str.getBytes().length) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (UnsupportedEncodingException e) {
            flag = false;
        }
        return flag;
    }


    /**
     * 验证设备用户名
     *
     * @param str
     * @return
     */
    public static boolean checkDeviceUsername(String str) {
        boolean flag = false;
        try {
            byte[] b = str.getBytes("UTF-8");
            str = new String(b, "UTF-8");
            Pattern pattern = Pattern.compile("^[A-Za-z0-9_.()\\+\\-]{1,16}$");
            Matcher matcher = pattern.matcher(str);
            if (matcher.matches()) {
                flag = true;
            } else {
                flag = false;
            }
        } catch (UnsupportedEncodingException e) {
            flag = false;
        }
        // if(0 < str.length() && 16 > str.length()){
        // flag = true;
        // }

        return flag;

    }


    /**
     * 直接验证设备的密码，走云视通，最长12位不能包含中文
     *
     * @param devPwd
     * @return
     */
    public static boolean checkDevPwd(String devPwd) {
        boolean right = false;
        try {
            byte[] byteArray = devPwd.getBytes("UTF-8");
            devPwd = new String(byteArray, "UTF-8");
//            Pattern pattern = Pattern.compile("[^\u4e00-\u9fa5]{0,12}$");
            Pattern pattern = Pattern.compile("^[A-Za-z0-9_.()\\+\\-]{1,12}$");
            Matcher matcher = pattern.matcher(devPwd);
            if (matcher.matches()) {
                right = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return right;
    }

    /**
     * 添加设备时验证密码长度可以为0，最长12位不能包含中文
     *
     * @param devPwd
     * @return
     */
    public static boolean checkAddDevPwd(String devPwd) {
        boolean right = false;
        try {
            byte[] byteArray = devPwd.getBytes("UTF-8");
            devPwd = new String(byteArray, "UTF-8");
//            Pattern pattern = Pattern.compile("[^\u4e00-\u9fa5]{0,12}$");
            Pattern pattern = Pattern.compile("^[A-Za-z0-9_.()\\+\\-]{0,12}$");
            Matcher matcher = pattern.matcher(devPwd);
            if (matcher.matches()) {
                right = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return right;
    }


    /**
     * 用户注册密码，找回密码，修改密码，登录密码的正则验证
     *
     * @param userPwd
     * @return
     */
    public static boolean checkUserPwd(String userPwd) {
        boolean right = false;
        try {

            if (null == userPwd) {
                return right;
            }

            if (userPwd.length() <= 20 && userPwd.length() >= 6) {
                right = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return right;
    }
}
