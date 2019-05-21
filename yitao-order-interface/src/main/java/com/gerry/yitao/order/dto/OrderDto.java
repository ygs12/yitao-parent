package com.gerry.yitao.order.dto;

import com.gerry.yitao.seller.dto.CartDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/14 19:33
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto implements Serializable {
    private Long addressId; // 收获人地址id
    private Integer paymentType;// 付款类型
    private List<CartDto> carts;// 订单详情
}