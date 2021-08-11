package com.chw.kill.controller;


import com.chw.kill.domain.User;
import com.chw.kill.result.RespBean;
import com.chw.kill.result.RespBeanEnum;
import com.chw.kill.service.IOrderService;
import com.chw.kill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author chw
 * @since 2021-06-10
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;

    /**
     * @Description: 订单详情
     * @param: [user, orderId]
     * @return: com.chw.kill.result.RespBean
     * @date: 2021/6/13 14:34
     */
    @RequestMapping("/detail")
    @ResponseBody
    public RespBean detail(User user,Long orderId){
        if(user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detailVo=orderService.getDetail(orderId);
        return RespBean.success(detailVo);
    }
}
