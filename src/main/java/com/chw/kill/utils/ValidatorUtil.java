package com.chw.kill.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Chihw
 * @Description 手机号校验
 * @Date 2021/6/7 23:05
 */
public class ValidatorUtil {
    //正则表达式判断是否合法
    private static final Pattern mobile_pattern=Pattern.compile("1\\d{10}");
    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher matcher=mobile_pattern.matcher(mobile);
        return matcher.matches();
    }
}
