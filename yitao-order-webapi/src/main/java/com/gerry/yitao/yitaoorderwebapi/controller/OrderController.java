package com.gerry.yitao.yitaoorderwebapi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gerry.yitao.common.entity.UserInfo;
import com.gerry.yitao.domain.Order;
import com.gerry.yitao.entity.PageResult;
import com.gerry.yitao.order.dto.OrderDto;
import com.gerry.yitao.order.service.OrderService;
import com.gerry.yitao.yitaoorderwebapi.filter.LoginInterceptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/9 17:02
 * @Description:
 */
@RestController
@RequestMapping("api/order")
public class OrderController {

    @Reference(timeout = 4000, check = false)
    private OrderService orderService;

    /**
     * 创建订单
     *
     * @param orderDto
     * @return
     */
    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody OrderDto orderDto) {
        // sign 签名保证数据不会被篡改
        UserInfo user = LoginInterceptor.getLoginUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(orderDto, user));
    }

    /**
     * 生成微信支付链接
     *
     * @param orderId
     * @return
     */
    @GetMapping("url/{id}")
    public ResponseEntity<String> generateUrl(@PathVariable("id") Long orderId) {
        UserInfo user = LoginInterceptor.getLoginUser();
        return ResponseEntity.status(HttpStatus.OK).body(orderService.generateUrl(orderId, user));
    }

    /**
     * 根据订单ID查询订单详情
     *
     * @param orderId
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Order> queryOrderById(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.queryById(orderId));
    }

    /**
     * 查询订单支付状态
     *
     * @param orderId
     * @return
     */
    @GetMapping("state/{id}")
    public ResponseEntity<Integer> queryOrderStateByOrderId(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok(orderService.queryOrderStateByOrderId(orderId));
    }

    /**
     * 分页查询所有订单
     *
     * @param page
     * @param rows
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<PageResult<Order>> queryOrderByPage(@RequestParam("page") Integer page,
                                                              @RequestParam("rows") Integer rows) {
        return ResponseEntity.ok(orderService.queryOrderByPage(page, rows));
    }
}
