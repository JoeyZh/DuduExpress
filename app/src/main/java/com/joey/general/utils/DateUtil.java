package com.joey.general.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    /**
     * 获取时间
     *
     * @return
     */
    public static String getTime() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        return sdFormatter.format(nowTime);
    }

    /**
     * 获取时间(内部用于查看任务执行时间)
     *
     * @return
     */
    public static String getInternalTime() {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("MM-dd HH:mm:ss SSS");
        return sdFormatter.format(nowTime);
    }
}
