package com.kry.pms.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class MD5Utils {

    private static final String SALT = "krypms";

    private static final String ALGORITH_NAME = "md5";

    private static final int HASH_ITERATIONS = 2;

//	public static String encrypt(String pswd) {
//		String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(SALT), HASH_ITERATIONS).toHex();
//		return newPassword;
//	}

    public static String encrypt(String username,String hotleCode, String pswd) {
        String newPassword = new SimpleHash(ALGORITH_NAME, pswd, ByteSource.Util.bytes(username + SALT+hotleCode),
                HASH_ITERATIONS).toHex();
        return newPassword;
    }
    public static void main(String[] args) {
        System.out.println(MD5Utils.encrypt("admin","0000", "123456"));
    }
}
