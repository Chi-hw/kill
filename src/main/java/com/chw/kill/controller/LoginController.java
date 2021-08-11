package com.chw.kill.controller;

import com.chw.kill.result.RespBean;
import com.chw.kill.service.IUserService;
import com.chw.kill.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/7 21:17
 */
//@RestController  默认为类下面的所有方法加@ResponseBody，就变成返回对象，不做页面跳转
@Controller
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @Autowired
    private IUserService userService;

    /**
     * @Description: 跳转登录
     * @param: []
     * @return: java.lang.String
     * @date: 2021/6/7 21:23
     */
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    /**
     * @Description: 登录功能
     * @param: [loginVo]
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/7 22:45
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public RespBean doLogin(@Valid LoginVo loginVo, HttpServletRequest request, HttpServletResponse response){
        //log.info("{}",loginVo);   //加@Slf4j注解可以直接使用log
        return userService.doLogin(loginVo,request,response);

    }
}
