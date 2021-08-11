package com.chw.kill.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.chw.kill.domain.KillGoods;
import com.chw.kill.domain.KillOrder;
import com.chw.kill.domain.Order;
import com.chw.kill.domain.User;
import com.chw.kill.mapper.KillOrderMapper;
import com.chw.kill.mapper.OrderMapper;
import com.chw.kill.service.IGoodsService;
import com.chw.kill.service.IKillGoodsService;
import com.chw.kill.service.IKillOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chw.kill.utils.MD5Util;
import com.chw.kill.utils.UUIDUtil;
import com.chw.kill.vo.GoodsVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author chw
 * @since 2021-06-10
 */
@Service
public class KillOrderServiceImpl extends ServiceImpl<KillOrderMapper, KillOrder> implements IKillOrderService {
    @Autowired
    private IKillOrderService killOrderService;
    @Autowired
    private IKillGoodsService killGoodsService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private KillOrderMapper killOrderMapper;
    
    /**
     * @Description: 生成订单与秒杀订单
     * @param: [user, goods]
     * @return: com.chw.kill.domain.Order
     * @date: 2021/6/10 23:01
     */
    @Transactional
    @Override
    public Order doKill(User user, GoodsVo goods) {
        KillGoods killGoods= killGoodsService.getOne(new QueryWrapper<KillGoods>().eq("goods_id",goods.getId()));
        killGoods.setStockCount(killGoods.getStockCount()-1);

        //killGoodsService.updateById(killGoods);

        //设置库存同时确保ID是更新的商品ID，gt确保库存大于0；
        boolean killGoodsResult=killGoodsService.update(new UpdateWrapper<KillGoods>().
                setSql("stock_count=stock_count-1").eq("goods_id",goods.getId()).gt("stock_count",0));
        /*if(!killGoodsResult){
            return null;
        }*/
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //判断是否还有库存？
        if(killGoods.getStockCount()<1){
            valueOperations.set("isStockEmpty:"+goods.getId(),"0");
            return null;
        }
        //生成订单
        Order order=new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliverAddId(0L);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(killGoods.getKillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        KillOrder killOrder=new KillOrder();
        killOrder.setUserId(user.getId());
        killOrder.setOrderId(order.getId());
        killOrder.setGoodsId(goods.getId());
        killOrderService.save(killOrder);
        //信息存入redis
        redisTemplate.opsForValue().set("order"+user.getId()+":"+goods.getId(),killOrder);

        return order;
    }
    /**
     * @Description: 获取秒杀结果
     * -1 表示失败， 0代表排队中
     * @param: [user, goodsId]
     * @return: java.lang.Long
     * @date: 2021/6/17 21:46
     */
    @Override
    public Long getResult(User user, Long goodsId) {
       KillOrder killOrder= killOrderMapper.selectOne(new QueryWrapper<KillOrder>().eq("user_id",user.getId()).eq("goods_id",goodsId));
       if (null!=killOrder){
           return killOrder.getOrderId();
       }else if(redisTemplate.hasKey("isStockEmpty:"+goodsId)){
           return -1L;
       }else {
           return 0L;
       }
    }

    @Override
    public String createPath(User user, Long goodsId) {
        String str= MD5Util.md5(UUIDUtil.uuid()+"123456");
        redisTemplate.opsForValue().set("killPath"+user.getId()+":"+goodsId,str,60, TimeUnit.SECONDS);
        return str;
    }

    @Override
    public boolean checkPath(User user, Long goodsId,String path) {
        if(user==null|| goodsId<0 || StringUtils.isEmpty(path)){
            return false;
        }
        String redisPath= (String) redisTemplate.opsForValue().get("killPath"+user.getId()+":"+goodsId);
        return path.equals(redisPath);
    }

    @Override
    public boolean checkCaptcha(User user, Long goodsId, String captcha) {
        if(StringUtils.isEmpty(captcha)||user ==null || goodsId<0){
            return false;
        }
        String redisCaptcha = (String) redisTemplate.opsForValue().get("captcha:" + user.getId() + ":" + goodsId);
        return captcha.equals(redisCaptcha);
    }

}
