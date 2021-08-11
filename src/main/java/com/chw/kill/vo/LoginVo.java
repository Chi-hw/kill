package com.chw.kill.vo;

import com.chw.kill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @Author Chihw
 * @Description  登录类
 * @Date 2021/6/7 22:41
 */
@Data
public class LoginVo {
    @NotNull
    @IsMobile(required = true)//默认是true可以不加
    private String mobile;

    @NotNull
    @Length(min=32)
    private String password;
}
