package com.gerry.yitao.user.service;

import com.gerry.yitao.domain.User;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/3 19:12
 * @Description: 用户操作接口
 */
public interface UserService {
    /**
     * 校验用户对象数据类型
     * @param data
     * @param type
     * @return
     */
    Boolean checkData(String data, Integer type);

    /**
     * 发送验证码
     * @param phone
     */
    void sendVerifyCode(String phone);

    /**
     * 用户注册
     * @param user
     * @param code
     */
    void register(User user, String code);

    /**
     * 根据账号和密码查询用户信息
     * @param username
     * @param password
     * @return
     */
    User queryUser(String username, String password);

}
