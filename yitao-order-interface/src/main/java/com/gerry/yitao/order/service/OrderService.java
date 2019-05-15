package com.gerry.yitao.order.service;

import com.gerry.yitao.common.entity.UserInfo;
import com.gerry.yitao.order.dto.OrderDto;
import com.gerry.yitao.domain.Order;
import com.gerry.yitao.entity.PageResult;

import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/14 19:40
 * @Description: 订单服务接口
 */
public interface OrderService {
    /**
     * 创建订单
     * @param orderDto
     * @return
     */
    Long createOrder(OrderDto orderDto, UserInfo user);

    /**
     * 生成支付链接
     * @param orderId
     * @return
     */
    String generateUrl(Long orderId, UserInfo user);

    /**
     * 根据订单号，查询订单信息
     * @param orderId
     * @return
     */
    Order queryById(Long orderId);

    /**
     * 根据订单编号查询订单状态码
     * @param orderId
     * @return
     */
    Integer queryOrderStateByOrderId(Long orderId);

    /**
     * 处理回到通知
     * @param msg
     */
    void handleNotify(Map<String, String> msg);

    /**
     * 分页查询订单信息
     * @param page
     * @param rows
     * @return
     */
    PageResult<Order> queryOrderByPage(Integer page, Integer rows);
}
