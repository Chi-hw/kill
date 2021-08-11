package com.chw.kill.vo;

import com.chw.kill.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author Chihw
 * @Description
 * @Date 2021/6/17 20:11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KillVo {
    private User user;
    private Long goodId;
}
