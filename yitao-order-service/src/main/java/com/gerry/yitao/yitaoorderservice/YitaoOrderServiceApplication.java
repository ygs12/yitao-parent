package com.gerry.yitao.yitaoorderservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.gerry.yitao.mapper")
//@EnableDistributedTransaction
public class YitaoOrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YitaoOrderServiceApplication.class);
    }

}
