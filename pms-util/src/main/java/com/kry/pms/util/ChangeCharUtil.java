package com.kry.pms.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeCharUtil {

    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    /**
     * 功能描述: <br>map的key下划线转驼峰
     * 〈〉
     * @Param: [map]
     * @Return: java.util.Map<java.lang.String,java.lang.String>
     * @Author: huanghaibin
     * @Date: 2020/4/11 17:19
     */
    public static Map<String, Object> humpToLineMap(Map<String, Object> map) {
        Map<String, Object> rmap = new HashMap<>();
        for (String key : map.keySet()) {//keySet获取map集合key的集合  然后在遍历key即可
            String value = map.get(key).toString();//
            String nkey = lineToHump(key);
            rmap.put(nkey, value);
        }
        return rmap;
    }

    /**
     * 驼峰转下划线,最后转为小写（大写）
     */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());//使用toLowerCase()方法实现小写转换
//            matcher.appendReplacement(sb, "_" + matcher.group(0).toUpperCase());//使用toUpperCase()方法实现大写转换
        }
        matcher.appendTail(sb);
        return sb.toString().toUpperCase();
    }

    /**
     * 下划线转驼峰,正常输出
     */
    public static String lineToHump(String str) {
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

}
