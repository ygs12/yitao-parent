package com.gerry.yitao.yitaosearchservice.listener;

import com.gerry.yitao.search.service.SearchService;
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
public class GoodsListener {
    @Autowired(required = false)
    private SearchService searchService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "yt.search.insert.queue", durable = "true"),
            exchange = @Exchange(name = "yt.item.exchange",
                    type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"),
            key = {"item.insert", "item.update"}
    ))
    public void listenInsert(Long id) {
        //监听新增或更新
        if (id != null) {
            System.out.println("处理insert/update消息");
            searchService.insertOrUpdate(id);
        }

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "yt.search.insert.queue", durable = "true"),
            exchange = @Exchange(name = "yt.item.exchange",
                    type = ExchangeTypes.TOPIC,
                    ignoreDeclarationExceptions = "true"),
            key = "item.delete"
    ))
    public void listenDelete(Long id) {
        //监听删除
        if (id != null) {
            System.out.println("处理delete消息");
            searchService.delete(id);
        }
    }
}

