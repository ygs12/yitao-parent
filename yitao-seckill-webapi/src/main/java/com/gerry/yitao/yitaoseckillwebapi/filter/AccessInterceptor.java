package com.gerry.yitao.yitaoseckillwebapi.filter;

import com.gerry.yitao.common.entity.UserInfo;
import com.gerry.yitao.common.util.JsonUtils;
import com.gerry.yitao.response.CodeMsg;
import com.gerry.yitao.response.Result;
import com.gerry.yitao.yitaoseckillwebapi.limiting.AccessLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/27 00:54
 * @Description: 接口限流拦截器
 */
@Component
public class AccessInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            AccessLimiter accessLimit = handlerMethod.getMethodAnnotation(AccessLimiter.class);
            if (accessLimit == null){
                // 不需要限流
                return true;
            }

            //获取用户信息
            UserInfo userInfo = LoginInterceptor.getLoginUser();
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            if (needLogin){
                if (userInfo == null){
                    render(response, CodeMsg.LOGIN_ERROR);
                    return false;
                }
                key += "_" + userInfo.getId();
            }

            String count = redisTemplate.opsForValue().get(key);
            if (count == null){
                redisTemplate.opsForValue().set(key,"1",seconds, TimeUnit.SECONDS);
            }else if(Integer.valueOf(count) < maxCount){
                redisTemplate.opsForValue().increment(key,1);
            }else {
                render(response,CodeMsg.ACCESS_LIMIT_REACHED);
            }
        }

        return super.preHandle(request, response, handler);
}

    /**
     * 输出响应结果
     * @param response
     * @param cm
     * @throws IOException
     */
    private void render(HttpServletResponse response, CodeMsg cm) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str  = JsonUtils.toString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}
