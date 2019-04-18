package com.gerry.yitao.upload.service;

import com.gerry.yitao.domain.Sku;
import com.gerry.yitao.domain.Spu;
import com.gerry.yitao.domain.SpuDetail;
import com.gerry.yitao.dto.CartDto;
import com.gerry.yitao.entity.PageResult;

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
}
