package com.gerry.yitao.yitaosellerservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gerry.yitao")
@EnableDubbo
public class YitaoSellerWebApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(YitaoSellerWebApiApplication.class);

    }
}
