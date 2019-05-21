package com.gerry.yitao.yitaosellerservice.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gerry.yitao.seller.bo.SeckillParameter;
import com.gerry.yitao.domain.SeckillGoods;
import com.gerry.yitao.domain.Sku;
import com.gerry.yitao.domain.Spu;
import com.gerry.yitao.domain.SpuDetail;
import com.gerry.yitao.seller.dto.CartDto;
import com.gerry.yitao.entity.PageResult;
import com.gerry.yitao.seller.service.GoodsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/16 22:51
 * @Description:
 */
@RestController
@RequestMapping("api/item")
public class GoodsController {


    @Reference(check = false,timeout = 60000)
    private GoodsService goodsService;

    @GetMapping("spu/page")
    public ResponseEntity<PageResult<Spu>> querySpuByPage(
            @RequestParam(value = "page",defaultValue = "1")Integer page,
            @RequestParam(value = "rows",defaultValue = "5")Integer rows,
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable", required = false)Boolean saleable
    ) {
        return ResponseEntity.ok(goodsService.querySpuByPage(page,rows,key,saleable));
    }

    /**
     * 查询spu详情
     * @param spuId
     * @return
     */
    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId") Long spuId) {
        return ResponseEntity.ok(goodsService.querySpuDetailBySpuId(spuId));
    }

    /**
     * 根据spuId查询商品详情
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkuBySpuId(@RequestParam("id") Long id) {
        return ResponseEntity.ok(goodsService.querySkuBySpuId(id));

    }

    /**
     * 根据sku ids查询sku
     * @param ids
     * @return
     */
    @GetMapping("sku/list/ids")
    public ResponseEntity<List<Sku>> querySkusByIds(@RequestParam("ids") List<Long> ids) {
        return ResponseEntity.ok(goodsService.querySkusByIds(ids));
    }


    /**
     * 删除商品
     * @param spuId
     * @return
     */
    @DeleteMapping("spu/spuId/{spuId}")
    public ResponseEntity<Void> deleteGoodsBySpuId(@PathVariable("spuId") Long spuId) {
        goodsService.deleteGoodsBySpuId(spuId);
        return ResponseEntity.ok().build();
    }


    /**
     * 添加商品
     * @param spu
     * @return
     */
    @PostMapping("goods")
    public ResponseEntity<Void> addGoods(@RequestBody Spu spu) {
        goodsService.addGoods(spu);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 更新商品
     * @param spu
     * @return
     */
    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody Spu spu) {
        goodsService.updateGoods(spu);
        return ResponseEntity.ok().build();
    }

    @PutMapping("spu/saleable")
    public ResponseEntity<Void> handleSaleable(@RequestBody Spu spu) {
        goodsService.handleSaleable(spu);
        return ResponseEntity.ok().build();

    }

    /**
     * 根据spuId查询spu及skus
     * @param spuId
     * @return
     */
    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuBySpuId(@PathVariable("id") Long spuId) {
        return ResponseEntity.ok(goodsService.querySpuBySpuId(spuId));
    }

    /**
     * 减库存
     * @param cartDtos
     * @return
     */
    @PostMapping("stock/decrease")
    public ResponseEntity<Void> decreaseStock(@RequestBody List<CartDto> cartDtos){
        goodsService.decreaseStock(cartDtos);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    ////////////////////// 秒杀 ///////////////////////////
    /**
     * 查询秒杀商品
     * @return
     */
    @GetMapping("/seckill/list")
    public ResponseEntity<List<SeckillGoods>> querySeckillGoods(){
        List<SeckillGoods> list = this.goodsService.querySeckillGoods();
        if (list == null || list.size() < 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 添加秒杀商品
     * @param seckillParameters
     * @return
     * @throws
     */
    @PostMapping("/seckill/add")
    public ResponseEntity<Boolean> addSeckillGoods(@RequestBody List<SeckillParameter> seckillParameters) throws ParseException {
        if (seckillParameters != null && seckillParameters.size() > 0){
            for (SeckillParameter seckillParameter : seckillParameters){
                this.goodsService.addSeckillGoods(seckillParameter);
            }
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok().build();
    }
}
