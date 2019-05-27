package com.gerry.yitao.yitaoorderservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.gerry.yitao.common.entity.UserInfo;
import com.gerry.yitao.domain.PayLog;
import com.gerry.yitao.mapper.PayLogMapper;
import com.gerry.yitao.order.service.PayLogService;
import com.gerry.yitao.order.status.PayStateEnum;
import com.gerry.yitao.yitaoorderservice.util.PayHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/15 16:45
 * @Description:
 */
@Slf4j
@Service(timeout = 4000)
public class PayLogServiceImpl implements PayLogService {

    @Autowired
    private PayLogMapper payLogMapper;

    @Autowired
    private PayHelper payHelper;

    public void createPayLog(Long orderId, Double actualPay, UserInfo user) {
        //创建支付对象
        PayLog payLog = new PayLog();
        payLog.setStatus(PayStateEnum.NOT_PAY.getValue());
        payLog.setPayType(1);
        payLog.setOrderId(orderId);
        payLog.setTotalFee(actualPay);
        payLog.setCreateTime(new Date());
        //获取用户信息
        payLog.setUserId(user.getId());

        payLogMapper.insertSelective(payLog);
    }

    @Transactional
    public Integer queryOrderStateByOrderId(Long orderId) {
        //优先去支付日志表中查询信息
        PayLog payLog = payLogMapper.selectByPrimaryKey(orderId);
        if (payLog == null || PayStateEnum.NOT_PAY.getValue() == payLog.getStatus()) {
            //未支付，调用微信接口查询订单支付状态
            return payHelper.queryPayState(orderId).getValue();
        }

        if (PayStateEnum.SUCCESS.getValue() == payLog.getStatus()) {
            //支付成功，返回1
            return PayStateEnum.SUCCESS.getValue();
        }

        //如果是其他状态，返回失败
        return PayStateEnum.FAIL.getValue();
    }
}

