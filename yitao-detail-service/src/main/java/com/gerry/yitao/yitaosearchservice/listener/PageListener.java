package com.gerry.yitao.yitaosearchservice.listener;

import com.gerry.yitao.detail.service.PageDetailService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/24 19:55
 * @Description:
 */
@Component
public class PageListener {
    @Autowired(required = false)
    private PageDetailService detailService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "yt.page.insert.queue", durable = "true"),
            exchange = @Exchange(value = "yt.item.exchange", type = ExchangeTypes.TOPIC,ignoreDeclarationExceptions = "true"),
            key ={"item.insert", "item.update"}
    ))
    public void listenInsert(Long id) {
        if (id != null) {
            //新增或修改
            detailService.createHtml(id);
        }
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "yt.page.delete.queue", durable = "true"),
            exchange = @Exchange(value = "yt.item.exchange", type = ExchangeTypes.TOPIC,ignoreDeclarationExceptions = "true"),
            key ={"item.delete"}
    ))
    public void listenDelete(Long id) {
        if (id != null) {
            //删除
            detailService.deleteHtml(id);
        }
    }
}

