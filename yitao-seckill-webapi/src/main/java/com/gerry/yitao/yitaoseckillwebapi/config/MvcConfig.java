package com.gerry.yitao.yitaoseckillwebapi.config;

import com.gerry.yitao.yitaoseckillwebapi.filter.AccessInterceptor;
import com.gerry.yitao.yitaoseckillwebapi.filter.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/9 19:09
 * @Description:
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer {

    @Autowired(required = false)
    private JwtProperties props;

    @Autowired
    private AccessInterceptor accessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> excludePath = new ArrayList<>();
        excludePath.add("/api/seckill/list");
        registry.addInterceptor(new LoginInterceptor(props))
                .addPathPatterns("/**").excludePathPatterns(excludePath);
        registry.addInterceptor(accessInterceptor);
    }
}