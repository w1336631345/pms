package com.kry.pms.pay;

import java.util.Random;

/**
 * @Description:
 * @Date: 2018/4/8
 * @Author: huanghaibin
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * StringUtils工具类方法
     * 获取一定长度的随机字符串，范围0-9，a-z
     *
     * @param length：指定字符串长度
     * @return 一定长度的随机字符串
     */
    public static String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String getRandom1(int len) {
        Random r = new Random();
        StringBuilder rs = new StringBuilder();
        for (int i = 0; i < len; i++) {
            rs.append(r.nextInt(10));
        }
        return rs.toString();
    }

    public static void main(String[] args) {
        System.out.println(getRandom1(15));
//        System.out.println( getRandomStringByLength(32));
    }
}
