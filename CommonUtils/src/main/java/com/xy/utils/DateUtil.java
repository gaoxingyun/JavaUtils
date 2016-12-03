package com.xy.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by xy on 2016/11/28.
 */
public class DateUtil {

    /**
     * 转换日期的格式
     *
     * @param sourceDate       源格式日期
     * @param sourceDateFormat 源日期格式
     * @param destDateFormat   目标日期格式
     * @return 目标格式日期
     * @throws ParseException 源日期异常
     */
    public static String convertDateFormat(String sourceDate, String sourceDateFormat, String destDateFormat)
            throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new SimpleDateFormat(sourceDateFormat).parse(sourceDate));
        String destDate = new SimpleDateFormat(destDateFormat).format(calendar.getTime());
        return destDate;
    }

}
