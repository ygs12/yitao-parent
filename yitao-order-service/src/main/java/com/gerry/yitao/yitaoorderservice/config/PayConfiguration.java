package com.gerry.yitao.yitaoorderservice.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/15 15:29
 * @Description: 支付配置类
 */
@SpringBootConfiguration
public class PayConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @ConfigurationProperties(prefix = "yt.wxpay")
    public WxPayConfig payConfig() {
        return new WxPayConfig();
    }
}
