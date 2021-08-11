package com.chw.kill.rabbitmq;

import com.chw.kill.domain.KillOrder;
import com.chw.kill.domain.User;
import com.chw.kill.result.RespBean;
import com.chw.kill.result.RespBeanEnum;
import com.chw.kill.service.IGoodsService;
import com.chw.kill.service.IKillGoodsService;
import com.chw.kill.service.IKillOrderService;
import com.chw.kill.service.IOrderService;
import com.chw.kill.utils.JsonUtil;
import com.chw.kill.vo.GoodsVo;
import com.chw.kill.vo.KillVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/17 20:42
 */
@Service
@Slf4j
public class KillReceiver {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IKillOrderService killOrderService;

    /**
     * @Description: 下单操作
     * @param: [message]
     * @return: void
     * @date: 2021/6/17 21:12
     */
    @RabbitListener(queues = "killQueue")
    public void receive(String message){
        log.info("接收的消息："+message);
        KillVo killVo= JsonUtil.jsonToPojo(message,KillVo.class);
        Long goodId=killVo.getGoodId();
        User user=killVo.getUser();
        //判断库存
        GoodsVo goodsVo=goodsService.findGoodsVoByGoodsId(goodId);
        if(goodsVo.getStockCount()<1){
            return;
        }
        KillOrder killOrder= (KillOrder) redisTemplate.opsForValue().get("order:"+user.getId()+":"+goodId);
        if (killOrder!=null){
            return  ;
        }
        //下单操作
        killOrderService.doKill(user,goodsVo);
    }
}
