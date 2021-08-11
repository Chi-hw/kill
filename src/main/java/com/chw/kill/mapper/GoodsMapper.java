package com.chw.kill.mapper;

import com.chw.kill.domain.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chw.kill.vo.GoodsVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author chw
 * @since 2021-06-10
 */
public interface GoodsMapper extends BaseMapper<Goods> {


    List<GoodsVo> findGoodsVo();

    GoodsVo findGoodsVoByGoodsId(Long goodsId);
}
