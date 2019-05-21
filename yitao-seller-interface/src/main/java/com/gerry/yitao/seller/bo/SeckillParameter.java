package com.gerry.yitao.seller.bo;


import lombok.Data;

import java.io.Serializable;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/16 19:29
 * @Description: 秒杀的参数类
 */
@Data

public class SeckillParameter implements Serializable {

    /**
     * 要秒杀的sku id
     */
    private Long id;

    /**
     * 秒杀开始时间
     */
    private String startTime;

    /**
     * 秒杀结束时间
     */
    private String endTime;

    /**
     * 参与秒杀的商品数量
     */
    private Integer count;

    /**
     * 折扣
     */
    private double  discount;
}
