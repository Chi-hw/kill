package com.chw.kill.vo;

import com.chw.kill.domain.Goods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author Chihw
 * @Description  商品返回对象
 * @Date 2021/6/10 15:27
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsVo extends Goods {
    private BigDecimal killPrice;
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
}
