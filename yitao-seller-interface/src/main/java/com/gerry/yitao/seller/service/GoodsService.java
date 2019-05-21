package com.gerry.yitao.seller.service;

import com.gerry.yitao.domain.SeckillGoods;
import com.gerry.yitao.domain.Sku;
import com.gerry.yitao.domain.Spu;
import com.gerry.yitao.domain.SpuDetail;
import com.gerry.yitao.entity.PageResult;
import com.gerry.yitao.seller.bo.SeckillParameter;
import com.gerry.yitao.seller.dto.CartDto;

import java.text.ParseException;
import java.util.List;

public interface GoodsService {
    PageResult<Spu> querySpuByPage(Integer page, Integer rows, String key, Boolean saleable);

    SpuDetail querySpuDetailBySpuId(Long spuId);

    List<Sku> querySkuBySpuId(Long spuId);

    void deleteGoodsBySpuId(Long spuId);

    void addGoods(Spu spu);

    void updateGoods(Spu spu);

    void handleSaleable(Spu spu);

    Spu querySpuBySpuId(Long spuId);

    List<Sku> querySkusByIds(List<Long> ids);

    void decreaseStock(List<CartDto> cartDtos);

    ///////// 秒杀 /////////////////
    /**
     * 查询秒杀商品
     * @return
     */
    List<SeckillGoods> querySeckillGoods();

    /**
     * 添加秒杀商品
     * @param seckillParameter
     */
    void addSeckillGoods(SeckillParameter seckillParameter) throws ParseException;
}
