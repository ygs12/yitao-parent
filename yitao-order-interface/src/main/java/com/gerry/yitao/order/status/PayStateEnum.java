package com.gerry.yitao.order.status;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/14 19:38
 * @Description: 支付状态枚举
 */
public enum PayStateEnum {

    NOT_PAY(0), SUCCESS(1), FAIL(2);

    int value;

    PayStateEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}