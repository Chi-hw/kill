package com.chw.kill.service;

import com.chw.kill.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chw.kill.result.RespBean;
import com.chw.kill.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chw
 * @since 2021-06-07
 */
public interface IUserService extends IService<User> {

    RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response);

    /**
     * @Description: 根据cookie获取用户
     * @param: [userTicket]
     * @return: com.chw.kill.domain.User
     * @date: 2021/6/9 20:23
     */
    User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response);
}
