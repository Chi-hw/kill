package com.chw.kill.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/7 19:43
 */
public class MD5Util {
    private static final String salt="1a2b3c4d";

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }
    /**
     * @Description: 第一次加密，避免在网络传输被截取然后反推出密码，所以在md5加密前先打乱密码
     * @param: [inputPass]
     * @return: java.lang.String
     * @date: 2021/6/7 20:01
     */
    public static String inputPassToFromPass(String inputPass){
        String str=""+salt.charAt(0)+ salt.charAt(2)+ inputPass +salt.charAt(5)+ salt.charAt(4);
        return md5(str);
    }
    /**
     * @Description: 第二次加密，用于存储到数据库
     * @param: []
     * @return: java.lang.String
     * @date: 2021/6/7 20:02
     */
    public static String formPassToDBPass(String formPass,String salt){
        String str=""+salt.charAt(0)+ salt.charAt(2)+ formPass+salt.charAt(5)+ salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass,String salt){
        String fromPass=inputPassToFromPass(inputPass);
        String dbPass=formPassToDBPass(fromPass,salt);
        return dbPass;
    }
    public static void main(String[] args) {
        System.out.println(inputPassToFromPass("123456"));   //第一次
        System.out.println(formPassToDBPass("d3b1294a61a07da9b49b6e22b2cbd7f9","1a2b3c4d"));  //第二次
        System.out.println(inputPassToDBPass("123456","1a2b3c4d"));   //存到数据库的密码

    }

}
