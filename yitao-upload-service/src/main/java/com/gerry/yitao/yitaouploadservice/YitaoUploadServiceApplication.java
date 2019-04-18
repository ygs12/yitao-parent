package com.gerry.yitao.yitaouploadservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class YitaoUploadServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(YitaoUploadServiceApplication.class);
    }

}
