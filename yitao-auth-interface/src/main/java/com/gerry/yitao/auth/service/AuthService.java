package com.gerry.yitao.auth.service;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/6 19:02
 * @Description:
 */
public interface AuthService {
    /**
     * 用户登录的方法
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);

}
