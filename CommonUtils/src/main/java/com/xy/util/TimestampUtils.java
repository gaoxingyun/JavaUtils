package com.xy.util;

import java.util.Date;

public class TimestampUtils {

    /**
     * 检查时间戳是否合法
     *
     * @param timestamp  待检验时间戳
     * @param errorValue 误差值
     * @return 时间戳是否合法
     */
    public static boolean checkTimestamp(long timestamp, int errorValue) {
        long serverTimestamp = getTimestamp();
        if (timestamp + errorValue > serverTimestamp && timestamp - errorValue < serverTimestamp) {
            return true;
        }
        return false;
    }

    /**
     * 获取系统时间戳，精确到秒
     *
     * @return 时间戳（秒）
     */
    public static long getTimestamp() {
        long serverTimestamp = new Date().getTime() / 1000;
        return serverTimestamp;
    }
}
