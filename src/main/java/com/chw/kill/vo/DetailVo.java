package com.chw.kill.vo;

import com.chw.kill.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/12 23:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailVo {
    private User user;
    private GoodsVo goodsVo;
    private int KillStatus;
    private int remainSeconds;
}
