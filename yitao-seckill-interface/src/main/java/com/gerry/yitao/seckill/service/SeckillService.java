package com.gerry.yitao.seckill.service;

import com.gerry.yitao.common.entity.UserInfo;
import com.gerry.yitao.domain.SeckillGoods;
import com.gerry.yitao.seckill.dto.SeckillDTO;

import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/23 18:40
 * @Description: 秒杀服务接口
 */
public interface SeckillService {

    String KEY_PREFIX_PATH = "yt:seckill:path";
    String KEY_PREFIX_STOCK = "yt:seckill:stock";
    String KEY_PREFIX_GOODS = "yt:seckill:goods";
    String KEY_PREFIX_VERIFY = "yt:verify:code";

    /**
     * 发送秒杀信息到队列当中接口方法
     * @param seckillDTO
     */
    void sendMessage(SeckillDTO seckillDTO);

    /**
     * 根据用户id查询秒杀订单接口方法
     * @param userId
     * @return
     */
    Long checkSeckillOrder(Long goodsId, Long userId);


    /**
     * 创建秒杀地址接口方法
     * @param goodsId
     * @param id
     * @return
     */
    String createPath(Long goodsId, Long id);

    /**
     * 验证秒杀地址接口方法
     * @param goodsId
     * @param id
     * @param path
     * @return
     */
    boolean checkSeckillPath(Long goodsId, Long id, String path);

    /**
     * 创建验证码
     */
    byte[] createVerifyCode(UserInfo user, long goodsId);

    /**
     * 校验验证码
     */
    boolean checkVerifyCode(UserInfo user, long goodsId, int verifyCode);

    /**
     * 查询可以秒杀的商品信息
     * @return
     */
    List<SeckillGoods> querySecKillList();

    /**
     * 从缓存中获取秒杀的商品详情
     * @param goodsId
     * @return
     */
    SeckillGoods queryGoodsInfoFormCache(Long goodsId);
}
