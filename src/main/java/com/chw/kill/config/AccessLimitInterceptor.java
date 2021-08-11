package com.chw.kill.config;

import com.chw.kill.domain.User;
import com.chw.kill.result.RespBean;
import com.chw.kill.result.RespBeanEnum;
import com.chw.kill.service.IUserService;
import com.chw.kill.utils.CookieUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/18 23:51
 */
@Component
public class AccessLimitInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(handler instanceof HandlerMethod){
            User user=getUser(request,response);
            UserContext.setUser(user);
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit=hm.getMethodAnnotation(AccessLimit.class);
            if(accessLimit==null){
                return true;
            }
            int second=accessLimit.second();
            int maxCount=accessLimit.maxCount();
            boolean needLogin=accessLimit.needLogin();
            String key=request.getRequestURI();
            if (needLogin){
                if (user==null){
                    render(response,RespBeanEnum.SESSION_ERROR);
                    return false;
                }
                key+=":"+user.getId();
            }
            ValueOperations valueOperations=redisTemplate.opsForValue();
            Integer count= (Integer) valueOperations.get(key);
            if (count==null){
                valueOperations.set(key,1,second, TimeUnit.SECONDS);
            }else if (count<maxCount){
                valueOperations.increment(key);
            }else {
                render(response,RespBeanEnum.ACCESS_LIMIT_REAHCED);
                return false;
            }
        }
        return true;
    }
    /**
     * @Description: 构建返回对象
     * @param: [response, error]
     * @return: void
     * @date: 2021/6/19 10:08
     */
    private void render(HttpServletResponse response, RespBeanEnum respBeanEnum) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out=response.getWriter();
        RespBean respBean=RespBean.error(respBeanEnum);
        out.write(new ObjectMapper().writeValueAsString(respBean));
        out.flush();
        out.close();
    }

    /**
     * @Description: 获取当前登录用户
     * @param: [request, response]
     * @return: com.chw.kill.domain.User
     * @date: 2021/6/19 9:58
     */
    private User getUser(HttpServletRequest request, HttpServletResponse response) {
        String ticket= CookieUtil.getCookieValue(request,"userticket");
        if(StringUtils.isEmpty(ticket)){
            return null;
        }
        return userService.getUserByCookie(ticket,request,response);
    }
}
