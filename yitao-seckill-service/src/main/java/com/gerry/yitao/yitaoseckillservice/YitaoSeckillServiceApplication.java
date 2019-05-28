package com.gerry.yitao.yitaoseckillservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.gerry.yitao.mapper")
public class YitaoSeckillServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(YitaoSeckillServiceApplication.class);
    }

}
