package com.gerry.yitao.cart.service;

import com.gerry.yitao.cart.entity.Cart;

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
    void addCart(Cart cart);

    /**
     * 查询购物车
     * @return
     */
    List<Cart> listCart();

    /**
     * 根据id更新商品数量
     * @param id
     * @param num
     */
    void updateNum(Long id, Integer num);

    /**
     * 删除购物车商品
     * @param id
     */
    void deleteCart(Long id);

    /**
     * 批量删除购物车商品
     * @param ids
     * @param userId
     */
    void deleteCarts(List<Object> ids, Integer userId);
}
