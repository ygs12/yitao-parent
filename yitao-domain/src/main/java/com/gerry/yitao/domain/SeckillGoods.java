package com.gerry.yitao.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/15 21:41
 * @Description: 秒杀商品类
 */
@Data
@ToString
@Table(name = "tb_seckill_sku")
public class SeckillGoods implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 秒杀商品的id
     */
    private Long skuId;
    /**
     * 秒杀开始时间
     */
    private Date startTime;
    /**
     * 秒杀结束时间
     */
    private Date endTime;

    /**
     * 获取服务端当前时间
     */
    @Transient
    private Date currentTime;

    /**
     * 秒杀价格
     */
    private Double seckillPrice;
    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品图片
     */
    private String image;

    /**
     * 是否可以秒杀
     */
    private Boolean enable;

    /**
     * 秒杀库存
     */
    @Transient
    private Integer stock;

    /**
     * 原价
     */
    @Transient
    private Double price;

    /**
     * 秒杀总数
     */
    @Transient
    private Integer seckillTotal;
}
