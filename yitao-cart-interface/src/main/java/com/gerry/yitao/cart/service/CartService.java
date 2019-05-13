package com.gerry.yitao.cart.service;

import com.gerry.yitao.cart.entity.Cart;
import com.gerry.yitao.common.entity.UserInfo;

import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/3 19:12
 * @Description:
 */
public interface CartService {
    /**
     * 添加到购物车
     * @param cart
     */
    void addCart(Cart cart, UserInfo user);

    /**
     * 批量添加商品到购物
     * @param carts
     * @param loginUser
     */
    void addCarts(List<Cart> carts,UserInfo loginUser);

    /**
     * 查询购物车
     * @return
     */
    List<Cart> listCart(UserInfo user);

    /**
     * 根据id更新商品数量
     * @param id
     * @param num
     */
    void updateNum(Long id, Integer num,UserInfo user);

    /**
     * 删除购物车商品
     * @param id
     */
    void deleteCart(Long id,UserInfo user);

    /**
     * 批量删除购物车商品
     * @param ids
     * @param userId
     */
    void deleteCarts(List<Object> ids, Integer userId);
}
