package com.gerry.yitao.yitaoorderwebapi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gerry.yitao.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/15 16:45
 * @Description:
 */
@RestController
@Slf4j
public class PayNotifyController {

    @Reference(timeout = 4000, check = false)
    private OrderService orderService;

    @PostMapping(value = "/wxpay/notify",produces = "application/xml")
    public ResponseEntity<String> payNotify(@RequestBody Map<String, String> msg) {
        //处理回调结果
        orderService.handleNotify(msg);
        // 没有异常，则返回成功
        String result = "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";
        return ResponseEntity.ok(result);

    }
}
