package com.gerry.yitao.domain;

import lombok.Data;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/15 21:41
 * @Description:
 */
@Data
@Table(name = "tb_order_detail")
public class OrderDetail implements Serializable {
    @Id
    @KeySql(useGeneratedKeys = true)
    private long id;
    private long orderId;
    private long skuId;
    private int num;
    private String title;
    private String ownSpec;
    private long price;
    private String image;
}
