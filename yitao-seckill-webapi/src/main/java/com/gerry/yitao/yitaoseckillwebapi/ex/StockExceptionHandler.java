package com.gerry.yitao.yitaoseckillwebapi.ex;

import com.gerry.yitao.domain.SeckillGoods;
import com.gerry.yitao.seckill.service.SeckillService;
import com.gerry.yitao.yitaoseckillwebapi.entity.StockParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/28 21:22
 * @Description:
 */
@ControllerAdvice
@Slf4j
public class StockExceptionHandler {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @ExceptionHandler(StockException.class)
    public void handlerGoodsStockForRedis(StockException e) {
        System.out.println("执行回滚补偿");
        StockParam param = e.getParam();
        BoundHashOperations<String,Object,Object> hashOperations = stringRedisTemplate.boundHashOps(SeckillService.KEY_PREFIX_STOCK);
        hashOperations.put(param.getGoodsId().toString(), param.getStock().toString());

        //3.1 更新缓存的商品信息库存
        BoundHashOperations<String,Object,Object> goodsOps = redisTemplate.boundHashOps(SeckillService.KEY_PREFIX_GOODS);
        SeckillGoods seckillGoods = (SeckillGoods) goodsOps.get(param.getGoodsId());
        seckillGoods.setStock(param.getStock().intValue());
        goodsOps.put(param.getGoodsId(),seckillGoods);

        log.info("减库存出现异常，SKUID:{}库存重置为:{}件",param.getGoodsId(), param.getStock());
    }
}
