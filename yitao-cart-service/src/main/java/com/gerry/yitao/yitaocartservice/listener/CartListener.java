package com.gerry.yitao.yitaocartservice.listener;

import com.gerry.yitao.cart.service.CartService;
import com.gerry.yitao.common.util.JsonUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/3 19:10
 * @Description: 异步删除购物车商品信息
 */
@Component
public class CartListener {

    @Autowired(required = false)
    private CartService cartService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "yt.cart.delete.queue", durable = "true"),
            exchange = @Exchange(name = "yt.cart.exchange",
                    type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"),
            key = {"cart.delete"}
    ))
    public void listenDelete(String params) {
        Map<String, Object> map = JsonUtils.toMap(params, String.class, Object.class);
        List<Object> ids = (List<Object>) map.get("skuIds");
        Integer userId = (Integer) map.get("userId");
        cartService.deleteCarts(ids, userId);
    }
}