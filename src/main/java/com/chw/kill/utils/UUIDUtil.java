package com.chw.kill.utils;
import java.util.UUID;
/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/8 21:39
 */
public class UUIDUtil {
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");//去掉原生的"-"
    }
}
