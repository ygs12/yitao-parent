package com.gerry.yitao.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/16 19:30
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDto implements Serializable {
    private Long skuId;  //商品skuId
    private Integer num;  //购买数量
}
