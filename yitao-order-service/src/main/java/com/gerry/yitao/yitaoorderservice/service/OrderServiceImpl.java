package com.gerry.yitao.yitaoorderservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.gerry.yitao.common.entity.UserInfo;
import com.gerry.yitao.common.exception.ServiceException;
import com.gerry.yitao.common.util.IdWorker;
import com.gerry.yitao.common.util.JsonUtils;
import com.gerry.yitao.domain.*;
import com.gerry.yitao.entity.PageResult;
import com.gerry.yitao.mapper.OrderDetailMapper;
import com.gerry.yitao.mapper.OrderMapper;
import com.gerry.yitao.mapper.OrderStatusMapper;
import com.gerry.yitao.mapper.PayLogMapper;
import com.gerry.yitao.order.dto.AddressDTO;
import com.gerry.yitao.order.dto.OrderDto;
import com.gerry.yitao.order.service.OrderService;
import com.gerry.yitao.order.service.PayLogService;
import com.gerry.yitao.order.status.OrderStatusEnum;
import com.gerry.yitao.order.status.PayStateEnum;
import com.gerry.yitao.yitaoorderservice.client.AddressClient;
import com.gerry.yitao.yitaoorderservice.client.GoodsClient;
import com.gerry.yitao.yitaoorderservice.util.PayHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/14 19:52
 * @Description:
 */
@Slf4j
@Service(timeout = 4000)
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private PayLogMapper payLogMapper;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private PayHelper payHelper;

    @Autowired
    private PayLogService payLogService;

    @Autowired
    private AmqpTemplate amqpTemplate;


    @Transactional
    public Long createOrder(OrderDto orderDto, UserInfo user) {
        //生成订单ID，采用自己的算法生成订单ID
        long orderId = idWorker.nextId();

        //填充order
        Order order = new Order();
        order.setCreateTime(new Date());
        order.setOrderId(orderId);
        order.setPaymentType(orderDto.getPaymentType());
        order.setPostFee(0L);  // TODO 调用物流信息，根据地址计算邮费

        //设置用户信息
        order.setUserId(user.getId());
        order.setBuyerNick(user.getUsername());
        order.setBuyerRate(false);  //TODO 卖家为留言

        //收货人地址信息，应该从数据库中物流信息中获取，这里使用的是假的数据
        AddressDTO addressDTO = AddressClient.findById(orderDto.getAddressId());
        if (addressDTO == null) {
            // 商品不存在，抛出异常
            throw new ServiceException("收货地址不存在");
        }
        order.setReceiver(addressDTO.getName());
        order.setReceiverAddress(addressDTO.getAddress());
        order.setReceiverCity(addressDTO.getCity());
        order.setReceiverDistrict(addressDTO.getDistrict());
        order.setReceiverMobile(addressDTO.getPhone());
        order.setReceiverZip(addressDTO.getZipCode());
        order.setReceiverState(addressDTO.getState());


        //付款金额相关，首先把orderDto转化成map，其中key为skuId,值为购物车中该sku的购买数量
        Map<Long, Integer> skuNumMap = orderDto.getCarts().stream().collect(Collectors.toMap(c -> c.getSkuId(), c -> c.getNum()));

        //查询商品信息，根据skuIds批量查询sku详情
        List<Sku> skus = goodsClient.querySkusByIds(new ArrayList<>(skuNumMap.keySet()));

        if (CollectionUtils.isEmpty(skus)) {
            throw new ServiceException("查询的商品信息不存在");
        }

        Double totalPay = 0.0;

        //填充orderDetail
        List<OrderDetail> orderDetails = new ArrayList<>();

        //遍历skus，填充orderDetail
        for (Sku sku : skus) {
            // 获取购买商品数量
            Integer num = skuNumMap.get(sku.getId());
            // 计算金额
            totalPay += num * sku.getPrice();

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setOwnSpec(sku.getOwnSpec());
            orderDetail.setSkuId(sku.getId());
            orderDetail.setTitle(sku.getTitle());
            orderDetail.setNum(num);
            orderDetail.setPrice(sku.getPrice().longValue());
            // 获取商品展示第一张图片
            orderDetail.setImage(StringUtils.substringBefore(sku.getImages(), ","));

            orderDetails.add(orderDetail);
        }

        order.setActualPay((totalPay.longValue() + order.getPostFee()));  //todo 还要减去优惠金额
        order.setTotalPay(totalPay.longValue());

        //保存order
        orderMapper.insertSelective(order);

        //保存detail
        orderDetailMapper.insertList(orderDetails);


        //填充orderStatus
        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setStatus(OrderStatusEnum.INIT.value());
        orderStatus.setCreateTime(new Date());

        //保存orderStatus
        orderStatusMapper.insertSelective(orderStatus);

        //减库存（1、下订单减库存，2、支付完成后减库存）
        // TODO 需要处理强一致分布式事务
        goodsClient.decreaseStock(orderDto.getCarts());

        //删除购物车中已经下单的商品数据, 采用异步mq的方式通知购物车系统删除已购买的商品，传送商品ID和用户ID
        HashMap<String, Object> map = new HashMap<>();

        try {
            map.put("skuIds", skuNumMap.keySet());
            map.put("userId", user.getId());
            amqpTemplate.convertAndSend("yt.cart.exchange", "cart.delete", JsonUtils.toString(map));
        } catch (Exception e) {
            log.error("删除购物车消息发送异常，商品ID：{}", skuNumMap.keySet(), e);
        }

        log.info("生成订单，订单编号：{}，用户id：{}", orderId, user.getId());
        return orderId;

    }

    public String generateUrl(Long orderId, UserInfo user) {
        //根据订单ID查询订单
        Order order = queryById(orderId);
        //判断订单状态
        if (order.getOrderStatus().getStatus() != OrderStatusEnum.INIT.value()) {
            throw new ServiceException("订单状态异常");
        }

        //TODO 这里传入一份钱，用于测试使用，实际中使用订单中的实付金额
        String url = payHelper.createPayUrl(orderId, "易淘商城测试支付", /*order.getActualPay()*/1L);
        if (StringUtils.isBlank(url)) {
            throw new ServiceException("支付地址异常");
        }

        //优先去支付日志表中查询信息
        PayLog payLog = payLogMapper.selectByPrimaryKey(orderId);

        if (payLog == null) {
            //写入支付日志
            payLogService.createPayLog(orderId, order.getActualPay(), user);
        }

        return url;

    }

    public Order queryById(Long orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw new ServiceException("订单不存在");
        }
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        List<OrderDetail> orderDetails = orderDetailMapper.select(orderDetail);
        order.setOrderDetails(orderDetails);
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        order.setOrderStatus(orderStatus);
        return order;
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

    @Transactional
    public void handleNotify(Map<String, String> msg) {
        payHelper.handleNotify(msg);
    }

    public PageResult<Order> queryOrderByPage(Integer page, Integer rows) {
        //开启分页
        PageHelper.startPage(page, rows);
        Example example = new Example(Order.class);
        //查询订单
        List<Order> orders = orderMapper.selectByExample(example);

        //查询订单详情
        for (Order order : orders) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getOrderId());
            List<OrderDetail> orderDetailList = orderDetailMapper.select(orderDetail);

            order.setOrderDetails(orderDetailList);

            //查询订单状态
            OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(order.getOrderId());
            order.setOrderStatus(orderStatus);
        }

        PageInfo<Order> pageInfo = new PageInfo<>(orders);

        return new PageResult<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getList());
    }
}
