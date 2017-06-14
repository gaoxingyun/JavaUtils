package com.xy.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by xy on 2016/11/28.
 */
public class DateUtils {

    /**
     * 转换日期的格式
     *
     * @param sourceDate       源格式日期
     * @param sourceDateFormat 源日期格式
     * @param destDateFormat   目标日期格式
     * @return 目标格式日期
     * @throws ParseException
     */
    public static String convertDateFormat(String sourceDate, String sourceDateFormat, String destDateFormat)
            throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat(sourceDateFormat).parse(sourceDate));
        String destDate = new SimpleDateFormat(destDateFormat).format(calendar.getTime());
        return destDate;
    }


    /**
     * 字符串型日期加一
     *
     * @param date 日期（yyyyMMdd）
     * @return 加一天后的日期
     */
    public static String addOneDay(String date) throws ParseException {
        return addOneDayForamt(date,"yyyyMMdd",1);
    }

    /**
     * 指定格式字符串型日期加一
     *
     * @param date    日期
     * @param format  日期格式
     * @param addDate 添加的日期
     * @return 加一天后的日期
     */
    public static String addOneDayForamt(String date, String format, Integer addDate) throws ParseException {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat(format).parse(date));
        calendar.add(Calendar.DATE, addDate);
        String retDate = new SimpleDateFormat(format).format(calendar.getTime());
        return retDate;
    }

}
