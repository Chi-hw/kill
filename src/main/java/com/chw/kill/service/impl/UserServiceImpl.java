package com.chw.kill.service.impl;

import com.chw.kill.domain.User;
import com.chw.kill.exception.GlobalException;
import com.chw.kill.mapper.UserMapper;
import com.chw.kill.result.RespBean;
import com.chw.kill.result.RespBeanEnum;
import com.chw.kill.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.kill.utils.CookieUtil;
import com.chw.kill.utils.MD5Util;
import com.chw.kill.utils.UUIDUtil;
import com.chw.kill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chw
 * @since 2021-06-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;  //会报错，不影响
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * @Description: 登录的接口实现类
     * @param: [loginVo] 登录类
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/8 18:41
     */
    @Override
    public RespBean doLogin(LoginVo loginVo, HttpServletRequest request, HttpServletResponse response) {
        String mobile=loginVo.getMobile();
        String password=loginVo.getPassword();
        /*
        //校验用户名和密码是否为空
        if(StringUtils.isEmpty(mobile)||StringUtils.isEmpty(password)){
            return RespBean.error(RespBeanEnum.LOGIN_ERROR);
        }
        //校验手机号是否合法
        if(!ValidatorUtil.isMobile(mobile)){
            return RespBean.error(RespBeanEnum.MOBILE_ERROR);
        }*/
        //根据手机号获取用户
        User user=userMapper.selectById(mobile);
        if(null==user){
            //return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        if(!MD5Util.formPassToDBPass(password,user.getSalt()).equals(user.getPassword())){
            //return RespBean.error(RespBeanEnum.LOGIN_ERROR);
            throw new GlobalException(RespBeanEnum.LOGIN_ERROR);
        }
        //生成cookie
        String ticket= UUIDUtil.uuid();
        //request.getSession().setAttribute(ticket,user);
        //将用户信息存入redis中
        redisTemplate.opsForValue().set("user:"+ticket,user);
        CookieUtil.setCookie(request,response,"userticket",ticket);

        return RespBean.success(ticket);
    }
    /**
     * @Description: 由cookie得到用户信息，对象缓存到redis
     * @param: [userTicket, request, response]
     * @return: com.chw.kill.domain.User
     * @date: 2021/6/9 20:40
     */
    @Override
    public User getUserByCookie(String userTicket,HttpServletRequest request,HttpServletResponse response) {
       if(StringUtils.isEmpty(userTicket)){
           return null;
       }
       User user= (User) redisTemplate.opsForValue().get("user:"+userTicket);
       if(user!=null){
           CookieUtil.setCookie(request,response,"userticker",userTicket);
       }
        return user;
    }
    /**
     * @Description: 更新密码，演示用户更新时redis中的操作，未用到
     * @param: [userTicket, password, request, response]
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/12 21:56
     */
    public RespBean updatePassword(String userTicket,String password,HttpServletRequest request,HttpServletResponse response){
        User user=getUserByCookie(userTicket,request,response);
        if(user==null){
            throw new GlobalException(RespBeanEnum.MOBILE_NOT_EXIST);
        }
        user.setPassword(MD5Util.inputPassToDBPass(password,user.getSalt()));
        int result=userMapper.updateById(user);
        if(1==result){
            //删除redis中数据
            redisTemplate.delete("user"+userTicket);
            return RespBean.success();
        }
        return RespBean.error(RespBeanEnum.PASSWORD_UPDATE_FAIL);
    }

}
