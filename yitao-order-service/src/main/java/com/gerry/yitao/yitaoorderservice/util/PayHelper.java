package com.gerry.yitao.yitaoorderservice.util;

import com.gerry.yitao.common.exception.ServiceException;
import com.gerry.yitao.domain.Order;
import com.gerry.yitao.domain.OrderStatus;
import com.gerry.yitao.domain.PayLog;
import com.gerry.yitao.mapper.OrderMapper;
import com.gerry.yitao.mapper.OrderStatusMapper;
import com.gerry.yitao.mapper.PayLogMapper;
import com.gerry.yitao.order.status.OrderStatusEnum;
import com.gerry.yitao.order.status.PayStateEnum;
import com.gerry.yitao.yitaoorderservice.config.WxPayConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/15 15:07
 * @Description: 支付工具类
 */
@Slf4j
@Component
public class PayHelper {
    private WXPay wxPay;

    private String notifyUrl;

    @Autowired
    private WxPayConfig payConfig;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderStatusMapper statusMapper;

    @Autowired
    private PayLogMapper payLogMapper;

    public PayHelper(WxPayConfig payConfig) {
        wxPay = new WXPay(payConfig);
        this.notifyUrl = payConfig.getNotifyUrl();
    }

    /**
     * 调用统一下单微信支付接口获取对应支付链接
     * @param orderId
     * @param description
     * @param totalPay
     * @return
     */
    public String createPayUrl(Long orderId, String description, Long totalPay) {
        //从缓存中取出支付连接
        String key = "order:pay:url:" + orderId;
        try {
            String url = redisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(url)) {
                return url;
            }
        } catch (Exception e) {
            log.error("查询缓存付款链接异常，订单号：{}", orderId, e);
        }

        try {
            // 真正发起预支付
            HashMap<String, String> data = new HashMap<>();
            //描述
            data.put("body", description);
            //订单号
            data.put("out_trade_no", orderId.toString());
            //货币（默认就是人民币）
            data.put("fee_type", "CNY");
            //总金额
            data.put("total_fee", totalPay.toString());
            //调用微信支付的终端ip
            data.put("spbill_create_ip", "127.0.0.1");
            //回调地址
            data.put("notify_url", notifyUrl);
            //交易类型为扫码支付
            data.put("trade_type", "NATIVE");
            // 需要按照字母先后顺序排序
            // 签名: sign = MD5.md5encode(body=fdsfs&out_trade_no=xxxxx&key=5d9f0cc242b22326934f)
            //填充请求参数并签名，并把请求参数处理成字节
            byte[] request = WXPayUtil.mapToXml(wxPay.fillRequestData(data)).getBytes("UTF-8");

            //发起请求并获取响应结果
            String xml = restTemplate.postForObject(WXPayConstants.UNIFIEDORDER_URL, request, String.class);

            //将结果处理成map
            Map<String, String> result = WXPayUtil.xmlToMap(xml);


            //通信失败
            if (WXPayConstants.FAIL.equals(result.get("return_code"))) {
                log.error("【微信下单】与微信通信失败，失败信息：{}", result.get("return_msg"));
                return null;
            }

            //下单失败
            if (WXPayConstants.FAIL.equals(result.get("result_code"))) {
                log.error("【微信下单】创建预交易订单失败，错误码：{}，错误信息：{}",
                        result.get("err_code"), result.get("err_code_des"));
                return null;
            }

            //检验签名
            isSignatureValid(result);

            //下单成功，获取支付连接
            String url = result.get("code_url");

            //将连接缓存到Redis中，失效时间2小时
            try {
                this.redisTemplate.opsForValue().set(key, url, 2, TimeUnit.HOURS);
            } catch (Exception e) {
                log.error("【微信下单】缓存付款链接异常,订单编号：{}", orderId, e);
            }

            return url;
        } catch (Exception e) {
            log.error("【微信下单】创建预交易订单异常", e);
            return null;
        }
    }

    /**
     * 检验签名
     *
     * @param result
     */
    private void isSignatureValid(Map<String, String> result) {
        try {
            boolean boo1 = WXPayUtil.isSignatureValid(result, payConfig.getKey(), WXPayConstants.SignType.HMACSHA256);
            boolean boo2 = WXPayUtil.isSignatureValid(result, payConfig.getKey(), WXPayConstants.SignType.MD5);

            if (!boo1 && !boo2) {
                throw new ServiceException("微信支付签名无效");
            }
        } catch (Exception e) {
            log.error("【微信支付】检验签名失败，数据：{}", result);
            throw new ServiceException("【微信支付】检验签名失败");
        }
    }

    /**
     * 处理支付回调结果
     *
     * @param msg
     */
    public void handleNotify(Map<String, String> msg) {
        //检验签名
        isSignatureValid(msg);

        //检验金额
        //解析数据
        String totalFee = msg.get("total_fee");  //订单金额
        String outTradeNo = msg.get("out_trade_no");  //订单编号
        String transactionId = msg.get("transaction_id");  //商户订单号
        String bankType = msg.get("bank_type");  //银行类型
        if (StringUtils.isBlank(totalFee) || StringUtils.isBlank(outTradeNo)
                || StringUtils.isBlank(transactionId) || StringUtils.isBlank(bankType)) {
            log.error("【微信支付回调】支付回调返回数据不正确");
            throw new ServiceException("支付回调返回数据不正确");
        }

        //查询订单
        Order order = orderMapper.selectByPrimaryKey(Long.valueOf(outTradeNo));

        //todo 这里验证回调数据时，支付金额使用1分进行验证，后续使用实际支付金额验证
        if (/*order.getActualPay()*/1 != Long.valueOf(totalFee)) {
            log.error("【微信支付回调】支付回调返回数据不正确");
            throw new ServiceException("支付参数不正常");

        }

        //判断支付状态
        OrderStatus orderStatus = statusMapper.selectByPrimaryKey(Long.valueOf(outTradeNo));

        if (orderStatus.getStatus() != OrderStatusEnum.INIT.value()) {
            //支付成功
            return;
        }

        //修改支付日志状态
        PayLog payLog = payLogMapper.selectByPrimaryKey(order.getOrderId());
        //未支付的订单才需要更改
        if (payLog.getStatus() == PayStateEnum.NOT_PAY.getValue()) {
            payLog.setOrderId(order.getOrderId());
            payLog.setBankType(bankType);
            payLog.setPayTime(new Date());
            payLog.setTransactionId(transactionId);
            payLog.setStatus(PayStateEnum.SUCCESS.getValue());
            payLogMapper.updateByPrimaryKeySelective(payLog);
        }


        //修改订单状态
        OrderStatus orderStatus1 = new OrderStatus();
        orderStatus1.setStatus(OrderStatusEnum.PAY_UP.value());
        orderStatus1.setOrderId(order.getOrderId());
        orderStatus1.setPaymentTime(new Date());
        statusMapper.updateByPrimaryKeySelective(orderStatus1);
    }


    /**
     * 查询订单支付状态
     *
     * @param orderId
     * @return
     */
    public PayStateEnum queryPayState(Long orderId) {
        Map<String, String> data = new HashMap<>();
        data.put("out_trade_no", orderId.toString());
        try {
            Map<String, String> result = wxPay.orderQuery(data);
            if (CollectionUtils.isEmpty(result) || WXPayConstants.FAIL.equals(result.get("return_code"))) {
                //未查询到结果，或连接失败
                log.error("【支付状态查询】链接微信服务失败，订单编号：{}", orderId);
                return PayStateEnum.NOT_PAY;
            }

            //查询失败
            if (WXPayConstants.FAIL.equals(result.get("result_code"))) {
                log.error("【支付状态查询】查询微信订单支付结果失败，错误码：{}, 订单编号：{}", result.get("result_code"), orderId);
                return PayStateEnum.NOT_PAY;
            }

            //检验签名
            isSignatureValid(result);

            //查询支付状态
            String state = result.get("trade_state");
            if (StringUtils.equals("SUCCESS", state)) {
                //支付成功, 修改支付状态等信息
                handleNotify(result);
                return PayStateEnum.SUCCESS;
            } else if (StringUtils.equals("USERPAYING", state) || StringUtils.equals("NOTPAY", state)) {
                //未支付成功
                return PayStateEnum.NOT_PAY;
            } else {
                //其他返回付款失败
                return PayStateEnum.FAIL;
            }
        } catch (Exception e) {
            log.error("查询订单支付状态异常", e);
            return PayStateEnum.NOT_PAY;
        }
    }
}
