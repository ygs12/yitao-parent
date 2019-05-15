package com.gerry.yitao.order.service;

import com.gerry.yitao.common.entity.UserInfo;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/14 19:42
 * @Description: 支付日志接口
 */
public interface PayLogService {
    /**
     * 添加支付日志
     * @param orderId
     * @param actualPay
     */
    void createPayLog(Long orderId, Long actualPay, UserInfo user);
}
