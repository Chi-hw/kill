package com.chw.kill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chw.kill.domain.*;
import com.chw.kill.exception.GlobalException;
import com.chw.kill.mapper.OrderMapper;
import com.chw.kill.result.RespBeanEnum;
import com.chw.kill.service.IGoodsService;
import com.chw.kill.service.IKillGoodsService;
import com.chw.kill.service.IKillOrderService;
import com.chw.kill.service.IOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.kill.vo.GoodsVo;
import com.chw.kill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chw
 * @since 2021-06-10
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private OrderMapper orderMapper;
    /**
     * @Description: 订单详情
     * @param: [orderId]
     * @return: com.chw.kill.vo.OrderDetailVo
     * @date: 2021/6/13 13:53
     */
    @Override
    public OrderDetailVo getDetail(Long orderId) {
        if(orderId==null){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order=orderMapper.selectById(orderId);
        GoodsVo goodsVo=goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detailVo=new OrderDetailVo();
        detailVo.setOrder(order);
        detailVo.setGoodsVo(goodsVo);
        return detailVo;
    }
}
