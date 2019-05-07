package com.gerry.yitao.yitaosmsservice.listener;

import com.gerry.yitao.yitaosmsservice.utils.SmsUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/3 19:10
 * @Description: 异步发送短信类
 */
@Component
public class SmsListener {

    @Autowired
    private SmsUtil smsUtils;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "yt.sms.queue",durable = "true"),
            exchange = @Exchange(value = "yt.sms.exchange",ignoreDeclarationExceptions = "true"),
            key = {"sms.verify.code"}
    ))
    public void listenSms(Map<String,String> msg){
        if (msg == null || msg.size() <= 0){
            //不做处理
            return;
        }
        String phone = msg.get("phone");
        String code = msg.get("code");

        if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(code)){

            try {
                //发送消息
                this.smsUtils.sendSms(phone, code);
            }catch (Exception e){
                return;
            }
        }else {
            //不做处理
            return;
        }
    }
}
