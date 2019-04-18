package com.gerry.yitao.common.util;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 类描述：时间操作工具栏
 * @auther Gerry
 * @create 2018/03/31
 */
public class DateUtil {

    /**
     * 功能描述：根据时间和传过来的格式 格式化显示时间
     * @param date
     * @param pattern
     * @return
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "null";
        }
        if (pattern == null || pattern.equals("") || pattern.equals("null")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        return new java.text.SimpleDateFormat(pattern).format(date);
    }

    /**
     * 功能描述：实现日期的加减
     * @param date
     * @param day
     * @return
     */
    public static Date addDay(Date date,int day){
        GregorianCalendar calendar=new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(GregorianCalendar.DATE, day);
        return calendar.getTime();
    }

}
