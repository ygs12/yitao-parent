package com.gerry.yitao.yitaocartservicewebapi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gerry.yitao.cart.entity.Cart;
import com.gerry.yitao.cart.service.CartService;
import com.gerry.yitao.common.entity.UserInfo;
import com.gerry.yitao.yitaocartservicewebapi.filter.LoginInterceptor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/9 17:02
 * @Description:
 */
@RestController
@RequestMapping("api/cart")
public class CartController {

    @Reference(timeout = 4000, check = false)
    private CartService cartService;

    /**
     * 添加商品到购物车
     *
     * @param cart
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart) {
        UserInfo user = LoginInterceptor.getLoginUser();
        cartService.addCart(cart, user);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量添加商品到购物车
     *
     * @param carts
     * @return
     */
    @PostMapping("batch")
    public ResponseEntity<Void> addCarts(@RequestBody List<Cart> carts) {
        UserInfo user = LoginInterceptor.getLoginUser();
        cartService.addCarts(carts, user);
        return ResponseEntity.ok().build();
    }


    /**
     * 从购物车中删除商品
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable("id") Long id) {
        UserInfo user = LoginInterceptor.getLoginUser();
        cartService.deleteCart(id, user);
        return ResponseEntity.ok().build();
    }


    /**
     * 更新购物车中商品的数量
     *
     * @param id  商品ID
     * @param num 修改后的商品数量
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("id") Long id, @RequestParam("num") Integer num) {
        UserInfo user = LoginInterceptor.getLoginUser();
        cartService.updateNum(id, num, user);
        return ResponseEntity.ok().build();
    }


    /**
     * 查询购物车
     *
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Cart>> listCart() {
        UserInfo user = LoginInterceptor.getLoginUser();
        return ResponseEntity.ok(cartService.listCart(user));
    }
}
