package com.gerry.yitao.yitaosellerservice.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gerry.yitao.domain.Category;
import com.gerry.yitao.seller.service.BrandService;
import com.gerry.yitao.seller.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/16 22:51
 * @Description:
 */
@RestController
@RequestMapping("api/item/category")
public class CategoryController {

    @Reference(check = false,timeout = 40000)
    private CategoryService categoryService;

    @Reference
    private BrandService brandService;

    /**
     * 根据父类ID查询分类结果
     * @param pid
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoryByPid(@RequestParam(value = "pid",defaultValue = "0") Long pid) {
        List<Category> categoryList = categoryService.queryCategoryByPid(pid);
        return ResponseEntity.ok(categoryList);
    }

    /**
     * 根据品牌ID查询商品分类
     *
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryCategoryByBid(@PathVariable("bid") Long bid) {
        return ResponseEntity.ok(brandService.queryCategoryByBid(bid));
    }

    /**
     * 根据商品分类Ids查询分类
     * @param ids
     * @return
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(categoryService.queryCategoryByIds(ids));
    }

    /**
     * 根据cid3查询三级分类
     * @param id
     * @return
     */
    @GetMapping("all/level/{id}")
    public ResponseEntity<List<Category>> queryAllByCid3(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.queryAllByCid3(id));
    }
}