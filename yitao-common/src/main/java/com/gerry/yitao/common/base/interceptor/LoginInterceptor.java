package com.gerry.yitao.common.base.interceptor;

import com.gerry.yitao.common.base.interceptor.properties.AuthProperties;
import com.gerry.yitao.common.base.interceptor.properties.FilterProperties;
import com.gerry.yitao.common.util.CookieUtils;
import com.gerry.yitao.common.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/8 11:53
 * @Description:
 */
@Slf4j
@Component
@EnableConfigurationProperties({AuthProperties.class, FilterProperties.class})
public class LoginInterceptor implements WebMvcConfigurer {
    @Autowired(required = false)
    private AuthProperties props;
    @Autowired(required = false)
    private FilterProperties filterProps;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {
            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                String requestURI = request.getRequestURI();

                // 判断为白名单就放行
                if (isAllowPath(requestURI)) {
                    return true;
                } else {
                    // 校验token
                    String token = CookieUtils.getCookieValue(request, props.getCookieName());
                    try {
                        //从Token获取解析用户信息
                        JwtUtils.getUserInfo(props.getPublicKey(), token);

                        // 放行
                        return true;
                    } catch (Exception e) {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.getWriter().append(HttpStatus.UNAUTHORIZED.getReasonPhrase());
                        log.error("非法访问，未登录，地址：{}", request.getRemoteHost(), e);
                    }
                }

                return false;
            }
        });
    }

    /**
     * 判断请求URI是不是白名单中的URI
     *
     * @param requestURI
     * @return
     */
    private Boolean isAllowPath(String requestURI) {
        boolean flag = false;

        for (String allowPath : filterProps.getAllowPaths()) {
            if (requestURI.startsWith(allowPath)) {
                //允许
                flag = true;
                break;
            }
        }
        return flag;
    }
}
