package com.gerry.yitao.yitaosellerservice.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gerry.yitao.bo.BrandBo;
import com.gerry.yitao.domain.Brand;
import com.gerry.yitao.entity.PageResult;
import com.gerry.yitao.upload.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/16 21:36
 * @Description:
 */
@RestController
@RequestMapping("api/item/brand")
public class BrandController {

    @Reference(check = false, timeout = 4000)
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> queryBrandByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "rows", defaultValue = "8") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
            @RequestParam(value = "key", required = false) String key
    ) {

        return ResponseEntity.ok(brandService.queryBrandByPageAndSort(page, rows,sortBy, desc, key));
    }

    /**
     * 新增品牌
     *
     * @param brand
     * @param cids  品牌所在的分类ID（多个分类）
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addBrand(Brand brand, @RequestParam("cids") List<Long> cids) {
        brandService.saveBrand(brand, cids);
        return new ResponseEntity(HttpStatus.CREATED);
    }


    /**
     * 更新品牌
     *
     * @param brandBo
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateBrand(BrandBo brandBo) {
        brandService.updateBrand(brandBo);
        return ResponseEntity.ok().build();
    }

    /**
     * 删除品牌
     *
     * @param bid
     * @return
     */
    @DeleteMapping("bid/{bid}")
    public ResponseEntity<Void> deleteBrand(@PathVariable("bid") Long bid) {
        brandService.deleteBrand(bid);
        return ResponseEntity.ok().build();
    }

    /**
     * 根据分类ID查询品牌
     *
     * @param cid
     * @return
     */
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandByCid(@PathVariable("cid") Long cid) {
        return ResponseEntity.ok(brandService.queryBrandByCid(cid));
    }

    /**
     * 根据商品品牌ID查询品牌
     *
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public ResponseEntity<Brand> queryById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(brandService.queryBrandByBid(id));
    }

    /**
     * 根据ids查询品牌
     * @param ids
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Brand>> queryBrandsByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(brandService.queryBrandByIds(ids));
    }
}
