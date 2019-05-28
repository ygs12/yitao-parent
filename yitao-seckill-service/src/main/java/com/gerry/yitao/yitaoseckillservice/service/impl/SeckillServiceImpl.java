package com.gerry.yitao.yitaoseckillservice.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gerry.yitao.common.entity.UserInfo;
import com.gerry.yitao.common.util.JsonUtils;
import com.gerry.yitao.domain.SeckillGoods;
import com.gerry.yitao.domain.SeckillOrder;
import com.gerry.yitao.domain.Sku;
import com.gerry.yitao.domain.Stock;
import com.gerry.yitao.mapper.SeckillMapper;
import com.gerry.yitao.mapper.SeckillOrderMapper;
import com.gerry.yitao.mapper.SkuMapper;
import com.gerry.yitao.mapper.StockMapper;
import com.gerry.yitao.seckill.dto.SeckillDTO;
import com.gerry.yitao.seckill.service.SeckillService;
import com.gerry.yitao.seckill.utils.ImageUtil;
import com.gerry.yitao.yitaoseckillservice.utils.CodecUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import tk.mybatis.mapper.entity.Example;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/23 18:45
 * @Description:
 */
@Service(timeout = 40000)
@Slf4j
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillMapper seckillMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SeckillOrderMapper seckillOrderMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 发送消息到秒杀队列当中
     * @param seckillDTO
     */
    @Override
    public void sendMessage(SeckillDTO seckillDTO) {
        String json = JsonUtils.toString(seckillDTO);

        try {
            this.amqpTemplate.convertAndSend("order.seckill", json);
        }catch (Exception e){
            log.error("秒杀商品消息发送异常，商品id：{}",seckillDTO.getSeckillGoods().getSkuId(),e);
        }
    }

    /**
     * 根据用户id查询秒杀订单
     * @param userId
     * @return
     */
    @Override
    public Long checkSeckillOrder(Long goodsId, Long userId) {
        Example example = new Example(SeckillOrder.class);
        example.createCriteria().andEqualTo("userId",userId).andEqualTo("skuId", goodsId);
        List<SeckillOrder> seckillOrders = seckillOrderMapper.selectByExample(example);

        if (seckillOrders == null || seckillOrders.size() == 0){
            return null;
        }

        return seckillOrders.get(0).getOrderId();
    }

    /////////////////////////////优化秒杀地址隐藏防刷/////////////////////////////////////////
    /**
     * 创建秒杀地址
     * @param goodsId
     * @param id
     * @return
     */
    @Override
    public String createPath(Long goodsId, Long id) {
        String str = CodecUtils.md5Hex(goodsId.toString()+id,"12a3d4b56");
        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX_PATH);
        String key = id.toString() + "_" + goodsId;
        hashOperations.put(key,str);
        hashOperations.expire(60, TimeUnit.SECONDS);

        return str;
    }

    /**
     * 验证秒杀地址
     * @return
     */
    @Override
    public boolean checkSeckillPath(Long goodsId, Long id, String path) {
        String key = id.toString() + "_" + goodsId;
        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(KEY_PREFIX_PATH);
        String encodePath = (String) hashOperations.get(key);

        return path.equals(encodePath);
    }

    @Override
    public byte[] createVerifyCode(UserInfo user, long goodsId) {
        if(user == null || goodsId <=0) {
            return null;
        }
        int width = 80;
        int height = 32;
        //create the image
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        // set the background color
        g.setColor(new Color(0xDCDCDC));
        g.fillRect(0, 0, width, height);
        // draw the border
        g.setColor(Color.black);
        g.drawRect(0, 0, width - 1, height - 1);
        // create a random instance to generate the codes
        Random rdm = new Random();
        // make some confusion
        for (int i = 0; i < 50; i++) {
            int x = rdm.nextInt(width);
            int y = rdm.nextInt(height);
            g.drawOval(x, y, 0, 0);
        }
        // generate a random code
        String verifyCode = generateVerifyCode(rdm);
        g.setColor(new Color(0, 100, 0));
        g.setFont(new Font("Candara", Font.BOLD, 24));
        g.drawString(verifyCode, 8, 24);
        g.dispose();
        //把验证码存到redis中
        int rnd = calc(verifyCode);
        stringRedisTemplate.opsForValue().set(SeckillService.KEY_PREFIX_VERIFY+"_"+user.getId()+","+goodsId, rnd+"");
        stringRedisTemplate.expire(SeckillService.KEY_PREFIX_VERIFY+"_"+user.getId()+","+goodsId,1,TimeUnit.MINUTES);
        //输出图片
        return ImageUtil.imageToBytes(image,"JPEG");
    }

    @Override
    public boolean checkVerifyCode(UserInfo user, long goodsId, int verifyCode) {
        if(user == null || goodsId <=0) {
            return false;
        }

        String code = stringRedisTemplate.opsForValue().get(SeckillService.KEY_PREFIX_VERIFY+"_"+user.getId()+","+goodsId);
        Integer codeOld = Integer.valueOf(code);

        if(codeOld == null || codeOld - verifyCode != 0 ) {
            return false;
        }
        // 删除验证码
        stringRedisTemplate.delete(SeckillService.KEY_PREFIX_VERIFY+"_"+user.getId()+","+goodsId);

        return true;
    }

    private static int calc(String exp) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            ScriptEngine engine = manager.getEngineByName("JavaScript");
            return (Integer)engine.eval(exp);
        }catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static char[] ops = new char[] {'+', '-', '*'};
    /**
     * + - *
     * */
    private String generateVerifyCode(Random rdm) {
        int num1 = rdm.nextInt(10);
        int num2 = rdm.nextInt(10);
        int num3 = rdm.nextInt(10);
        char op1 = ops[rdm.nextInt(3)];
        char op2 = ops[rdm.nextInt(3)];
        String exp = ""+ num1 + op1 + num2 + op2 + num3;
        return exp;
    }


    //////////////////////////////////////////秒杀商品查询////////////////////////////////////////////////

    /**
     * 查询可以秒杀的商品列表
     * @return
     */
    public List<SeckillGoods> querySecKillList() {
        // 获取缓存中存储秒杀商品集合(优化处理)
        BoundHashOperations boundHashOps = this.redisTemplate.boundHashOps(KEY_PREFIX_GOODS);
        // 有坑, list不为空null，只是size=0
        List<SeckillGoods> list = boundHashOps.values();

        if (list.size() == 0) {
            //////////////////// 在数据库中查询///////////////////
            Example example = new Example(SeckillGoods.class);
            // 处理方式： 当前缓存中商品库存为0的时候，该商品就会从移除，并且设置enable为0
            // 当前时间
            Date currentDate = new Date();
            example.createCriteria().andLessThanOrEqualTo("startTime", currentDate)
                    .andGreaterThanOrEqualTo("endTime", currentDate)
                    .andEqualTo("enable", true);
            // 真正请求数据库处理
            list = this.seckillMapper.selectByExample(example);

            list.forEach(goods -> {
                Stock stock = this.stockMapper.selectByPrimaryKey(goods.getSkuId());
                goods.setStock(stock.getSeckillStock());
                goods.setSeckillTotal(stock.getSeckillTotal());
                // 查询原价
                Sku sku = this.skuMapper.selectByPrimaryKey(goods.getSkuId());
                goods.setPrice(sku.getPrice());
                // 放入缓存
                boundHashOps.put(goods.getSkuId(), goods);
            });
        }

        return list;
    }

    /**
     * 从缓存中获取秒杀的商品信息
     */
    public SeckillGoods queryGoodsInfoFormCache(Long goodsId) {
        // 获取缓存中获取Hash
        BoundHashOperations boundHashOps = this.redisTemplate.boundHashOps(KEY_PREFIX_GOODS);
        Date currentDate = new Date();
        SeckillGoods seckillGoods = (SeckillGoods) boundHashOps.get(goodsId);
        seckillGoods.setCurrentTime(currentDate);

        return seckillGoods;
    }
}
