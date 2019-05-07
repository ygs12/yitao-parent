package com.gerry.yitao.yitaoauthservice.client;

import com.gerry.yitao.common.util.RestTemplateUtils;
import com.gerry.yitao.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/6 19:07
 * @Description:
 */
public class UserClient {
    @Value("${client.url:}")
    private String url;

    public User queryUser(String username, String password) {
        String requestUrl = url+"api/user/query?username={username}&password={password}";
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        ResponseEntity<User> entity = RestTemplateUtils.get(requestUrl,User.class,params);

        return entity.getBody();
    }
}
