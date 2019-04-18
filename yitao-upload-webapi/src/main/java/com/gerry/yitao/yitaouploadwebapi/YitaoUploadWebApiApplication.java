package com.gerry.yitao.yitaouploadwebapi;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.gerry.yitao")
@EnableDubbo
public class YitaoUploadWebApiApplication {


    public static void main(String[] args) {
        SpringApplication.run(YitaoUploadWebApiApplication.class);

    }
}
