package com.gerry.yitao.domain;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/15 21:41
 * @Description:
 */
@Data
@Table(name = "tb_order")
public class Order implements Serializable {
    @Id
    private long orderId;
    private long totalPay;
    private long actualPay;
    private String promotionIds;
    private byte paymentType;
    private long postFee;
    private Date createTime;
    private String shippingName;
    private String shippingCode;
    private String userId;
    private String buyerMessage;
    private String buyerNick;
    private Byte buyerRate;
    private String receiverState;
    private String receiverCity;
    private String receiverDistrict;
    private String receiverAddress;
    private String receiverMobile;
    private String receiverZip;
    private String receiver;
    private Integer invoiceType;
    private Integer sourceType;
}
