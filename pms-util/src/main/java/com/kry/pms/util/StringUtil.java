package com.kry.pms.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * 
 * @author Louis Lueng
 *
 */
public class StringUtil {
	public static String captureName(String name) {
		char[] cs = name.toCharArray();
		cs[0] -= 32;
		return String.valueOf(cs);

	}

	//验证码生产方法
	public static String getYzm() {
		String yzm = "";
		for (int i = 0; i < 4; i++) {
			int int1 = new Random().nextInt(10);
			yzm += int1;
		}
		return yzm;
	}
	public static String toGBK(String source) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		byte[] bytes = source.getBytes("GBK");
		for (byte b : bytes) {
			sb.append("%" + Integer.toHexString((b & 0xff)).toUpperCase());
		}
		return sb.toString();
	}

	public static List<String> msgContent(String content){
		List<String> list = new ArrayList<>();
		String[] str = content.split("-");
		list = Arrays.asList(str);
		return list;
	}
}
