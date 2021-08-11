package com.chw.kill.service;

import com.chw.kill.domain.Order;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chw.kill.domain.User;
import com.chw.kill.vo.GoodsVo;
import com.chw.kill.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chw
 * @since 2021-06-10
 */
public interface IOrderService extends IService<Order> {
    //订单详情
    OrderDetailVo getDetail(Long orderId);
}
