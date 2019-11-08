package com.kry.pms.util;

public class DateUtil {
    /**
     * 得到当前时间戳
     * @return
     */
    public static Long getCurrentTimeStamp() {
        long timeMillis = System.currentTimeMillis();
        return timeMillis;
    }
}
