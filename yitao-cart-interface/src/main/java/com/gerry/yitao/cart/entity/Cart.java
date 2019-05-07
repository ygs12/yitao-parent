package com.gerry.yitao.cart.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/3 19:12
 * @Description:
 */
@Data
public class Cart implements Serializable {

    //商品id
    private Long skuId;

    //商品标题
    private String title;

    //购买数量
    private Integer num;

    //商品图片
    private String image;

    //加入购物车时商品的价格
    private Long price;

    //商品的规格参数
    private String ownSpec;
}
