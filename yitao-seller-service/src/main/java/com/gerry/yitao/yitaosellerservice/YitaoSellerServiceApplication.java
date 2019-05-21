package com.gerry.yitao.yitaosellerservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.gerry.yitao.mapper")
@EnableDistributedTransaction
public class YitaoSellerServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(YitaoSellerServiceApplication.class);
    }

}
