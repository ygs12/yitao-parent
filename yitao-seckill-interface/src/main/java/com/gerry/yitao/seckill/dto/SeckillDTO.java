package com.gerry.yitao.seckill.dto;

import com.gerry.yitao.common.entity.UserInfo;
import com.gerry.yitao.domain.SeckillGoods;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/23 18:38
 * @Description: 秒杀信息传输类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeckillDTO implements Serializable {
    /**
     * 用户信息
     */
    private UserInfo userInfo;

    /**
     * 秒杀商品
     */
    private SeckillGoods seckillGoods;
}
