package com.chw.kill.service;

import com.chw.kill.domain.Goods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.chw.kill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chw
 * @since 2021-06-10
 */
public interface IGoodsService extends IService<Goods> {
    List<GoodsVo> findGoodsVo();


    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
