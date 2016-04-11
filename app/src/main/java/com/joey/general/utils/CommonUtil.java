package com.joey.general.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CommonUtil {

    /**
     * 判断用户是否快速点击
     */
    static long lastClickTime = 0;

    /**
     * 检测手机号是否合法
     *
     * @param mobile 手机号
     * @return
     */
    public static boolean isMobileFormatLegal(String mobile) {
        int result = CheckPhoneNumber.matchNum(mobile);
        if (result == 4 || result == 5) {// 格式不合法
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测邮箱是否合法
     *
     * @param email
     * @return
     */
    public static boolean isEmailFormatLegal(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }

        boolean result = false;
        String strPattern = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$"; // 输入任意字符，但是必须要在（4～20）位之间

        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(email);
        result = m.matches();
        if (result) {
            if (email.length() <= 60) {
                result = true;
            } else {
                result = false;
            }
        }
        return result;
    }

    /**
     * 过滤表情符及特殊字符
     *
     * @param str
     * @return
     * @throws PatternSyntaxException
     */
    public static String stringFilter(String str) throws PatternSyntaxException {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        // 只允许字母和数字和中文//[\\pP‘’“”
        String regEx = "^[A-Za-z\\d\\u4E00-\\u9FA5\\p{P}‘’“”]+$";
        StringBuilder sb = new StringBuilder(str);

        for (int len = str.length(), i = len - 1; i >= 0; --i) {
            if (!Pattern.matches(regEx, String.valueOf(str.charAt(i)))) {
                sb.deleteCharAt(i);
            }
        }

        return sb.toString();
    }

    /**
     * 判断数组中是否包含某元素
     *
     * @param array
     * @param v
     * @return
     */
    public static <T> boolean contains(final T[] array, final T v) {
        for (final T e : array)
            if (e == v || v != null && v.equals(e))
                return true;

        return false;
    }

    /**
     * 判断快速点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= 1000) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }


}
