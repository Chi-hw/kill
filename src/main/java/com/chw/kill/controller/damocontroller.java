package com.chw.kill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/5 20:18
 */

@Controller
public class damocontroller {
    /**
     * @Description: 测试类
     * @param: [model]
     * @return: java.lang.String
     * @date: 2021/6/7 19:02
     */
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("name","world");
        return "test";
    }
}
