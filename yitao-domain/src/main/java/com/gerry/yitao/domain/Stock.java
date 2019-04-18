package com.gerry.yitao.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/15 21:41
 * @Description:
 */
@Table(name = "tb_stock")
@Data
public class Stock implements Serializable {

    @Id
    private Long skuId;

    private Integer seckillStock;// 秒杀可用库存

    private Integer seckillTotal;// 已秒杀数量

    private Integer stock;// 正常库存
}