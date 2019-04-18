package com.gerry.yitao.yitaosellerservice;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDubbo
@MapperScan(basePackages = "com.gerry.yitao.mapper")
public class YitaoSellerServiceApplication {


    public static void main(String[] args) {
        SpringApplication.run(YitaoSellerServiceApplication.class);

        /// 环境测试
        /*ConfigurableApplicationContext context = SpringApplication.run(YitaoSellerServiceApplication.class, args);
        DataSource bean = context.getBean(DataSource.class);
        System.out.println(bean);

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            System.out.println(beanDefinitionName);
        }

        BrandMapper bean1 = context.getBean(BrandMapper.class);
        System.out.println(bean1.selectAll());

        context.close();*/
    }

}
