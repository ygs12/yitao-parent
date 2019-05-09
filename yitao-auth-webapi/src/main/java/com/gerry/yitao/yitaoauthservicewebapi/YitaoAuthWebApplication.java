package com.gerry.yitao.yitaoauthservicewebapi;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.gerry.yitao.auth.entity.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class,scanBasePackages = {"com.gerry.yitao"})
@EnableDubbo
@EnableConfigurationProperties(JwtProperties.class)
public class YitaoAuthWebApplication {


    public static void main(String[] args) {
        SpringApplication.run(YitaoAuthWebApplication.class);
    }
}
