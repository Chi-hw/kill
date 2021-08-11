package com.chw.kill.service;

import com.chw.kill.domain.KillOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chw.kill.domain.Order;
import com.chw.kill.domain.User;
import com.chw.kill.vo.GoodsVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chw
 * @since 2021-06-10
 */
public interface IKillOrderService extends IService<KillOrder> {

    Order doKill(User user, GoodsVo goods);

    Long getResult(User user, Long goodsId);

    String createPath(User user, Long goodsId);

    boolean checkPath(User user, Long goodsId,String path);

    boolean checkCaptcha(User user, Long goodsId, String captcha);
}
