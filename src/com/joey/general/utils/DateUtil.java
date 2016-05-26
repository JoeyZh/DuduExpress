package com.joey.general.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public  static final String DATE_FORMMAT_STR_1 = "yyyy-MM-dd HH:mm:ss SSS";
    public  static final String DATE_FORMMAT_STR_2 = "MM-dd HH:mm:ss SSS";
    public  static final String DATE_FORMMAT_STR_DAY = "yyyy-MM-dd";
    public  static final String DATE_FORMMAT_STR_3 = "yyyy年MM月dd HH:mm:ss";
    public  static final String DATE_FORMMAT_STR_4 = "yyyyMMddHHmmss";

    /**
     * 获取时间
     *
     * @return
     */
    public static String getTime(String dateFormat) {
        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat(dateFormat);
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

    public static String convertToDateStr(String format,long timeMillis){
        Date date = new Date(timeMillis);
        SimpleDateFormat sdFormatter = new SimpleDateFormat(format);
        return sdFormatter.format(date);
    }
    
}
