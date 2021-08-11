package com.chw.kill.vo;

import com.chw.kill.utils.ValidatorUtil;
import com.chw.kill.validator.IsMobile;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author Chihw
 * @Description  自定义手机号码校验规则
 * 实现ConstraintValidator< 注解,类型>接口
 * @Date 2021/6/8 18:56
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String>{
    private boolean required =false;
    /**
     * @Description: 初始化
     * @param: [constraintAnnotation]
     * @return: void
     * @date: 2021/6/8 19:02
     */
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required=constraintAnnotation.required();  //获取到填的值
    }
    /**
     * @Description: 校验规则
     * @param: [s, constraintValidatorContext]
     * @return: boolean
     * @date: 2021/6/8 19:05
     */
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){ //必填
            return ValidatorUtil.isMobile(s);
        }else{        //非必填
            if(StringUtils.isEmpty(s)){
                return true;
            }else{
                return ValidatorUtil.isMobile(s);
            }
        }
    }
}
