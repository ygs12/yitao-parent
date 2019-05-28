package com.gerry.yitao.yitaoseckillwebapi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gerry.yitao.common.entity.UserInfo;
import com.gerry.yitao.domain.SeckillGoods;
import com.gerry.yitao.response.CodeMsg;
import com.gerry.yitao.response.Result;
import com.gerry.yitao.seckill.dto.SeckillDTO;
import com.gerry.yitao.seckill.service.SeckillService;
import com.gerry.yitao.seckill.utils.ImageUtil;
import com.gerry.yitao.yitaoseckillwebapi.entity.SecKillParam;
import com.gerry.yitao.yitaoseckillwebapi.entity.StockParam;
import com.gerry.yitao.yitaoseckillwebapi.ex.StockException;
import com.gerry.yitao.yitaoseckillwebapi.filter.LoginInterceptor;
import com.gerry.yitao.yitaoseckillwebapi.limiting.AccessLimiter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/25 22:24
 * @Description:
 */
@Controller
@RequestMapping("api/seckill")
public class SeckillController implements InitializingBean {
    @Reference(timeout = 50000, check = false)
    private SeckillService seckillService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    //
    private Map<Long,Boolean> localOverMap = new ConcurrentHashMap<>();



    @Override
    public void afterPropertiesSet() throws Exception {
        //1.查询可以秒杀的商品
        BoundHashOperations<String,Object,Object> hashOperations = this.stringRedisTemplate.boundHashOps(SeckillService.KEY_PREFIX_STOCK);

        if (hashOperations.getKey().equals(SeckillService.KEY_PREFIX_STOCK)){
            hashOperations.entries().forEach((m,n) -> localOverMap.put(Long.parseLong(m.toString()),false));
        }
    }

    /**
     * @param path
     * @param goodsId
     * @return 前端秒杀API
     */
    @PostMapping("/{path}/seck/{goodsId}")
    @ResponseBody
    public Result<Integer> secKillOrder(
            @PathVariable("path") String path,
            @PathVariable("goodsId") Long goodsId){
        // 获取登录的用户对象
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        // 获取秒杀的商品信息
        SeckillGoods seckillGoods = seckillService.queryGoodsInfoFormCache(goodsId);
        //1.验证路径
        boolean check = seckillService.checkSeckillPath(goodsId,userInfo.getId(),path);
        if (!check){
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        //2.内存标记，减少redis访问
        boolean over = localOverMap.get(goodsId);
        if (over){
            return Result.error(CodeMsg.MIAO_SHA_OVER);
        }

        //5. 判断是否已经重复秒杀
        Long result = seckillService.checkSeckillOrder(goodsId,userInfo.getId());
        if(result != null) {
            return Result.error(CodeMsg.REPEATE_MIAOSHA);
        }

        // AOP ， 异常拦截器
        // 创建库存对象
        StockParam param = new StockParam();

        try {
            //3.读取库存，减一后更新缓存
            BoundHashOperations<String,Object,Object> hashOperations = stringRedisTemplate.boundHashOps(SeckillService.KEY_PREFIX_STOCK);
            Long stock = hashOperations.increment(goodsId.toString(), -1);
            param.setStock(stock + 1);
            param.setGoodsId(goodsId);

            //3.1 更新缓存的商品信息库存
            BoundHashOperations<String,Object,Object> goodsOps = redisTemplate.boundHashOps(SeckillService.KEY_PREFIX_GOODS);
            //TODO JUC => ThreadLocale // 线程安全
            seckillGoods.setStock(seckillGoods.getStock() - 1);
            goodsOps.put(goodsId,seckillGoods);



            //4.库存不足直接返回
            if (stock < 0){
                localOverMap.put(goodsId,true);
                //TODO 缓存中商品要不要干掉？（异步下单处理中已经处理啦）
                return Result.error(CodeMsg.MIAO_SHA_OVER);
            }

            // 测试异常出现是否回滚补偿
            //throw new RuntimeException("ffddfd");

            //6.库存充足，请求入队
            //6.1 获取用户信息
            SeckillDTO dto = new SeckillDTO(userInfo,seckillGoods);

            //6.2 发送消息
            seckillService.sendMessage(dto);
        } catch (Exception e) {
            throw new StockException(param, "秒杀减库存出现异常");
        }

        return Result.success(0);
    }

    /**
     * 获取秒杀路径()
     * @param secKillParam
     * @return
     */
    @AccessLimiter(seconds = 5,maxCount = 5,needLogin = true)
    @PostMapping("getPath")
    @ResponseBody
    public Result<String> getSecKillPath(@RequestBody SecKillParam secKillParam){
        Long goodsId = secKillParam.getGoodsId();
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        if (userInfo == null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        boolean check = seckillService.checkVerifyCode(userInfo, goodsId, secKillParam.getVerifyCode());
        if(!check) {
            return Result.error(CodeMsg.REQUEST_ILLEGAL);
        }

        // 获取秒杀的地址组成可变部分
        String path = seckillService.createPath(goodsId,userInfo.getId());

        return Result.success(path);
    }

    @GetMapping(value = "/verifyCode")
    public void getSecKillVerifyCode(
            HttpServletResponse response,
            @RequestParam("goodsId")long goodsId) {
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        try {
            byte[] bytes = seckillService.createVerifyCode(userInfo, goodsId);
            BufferedImage image = ImageUtil.bytesToImage(bytes);
            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "JPEG", out);
            out.flush();
            out.close();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据userId查询秒杀订单号
     *
     * @param goodsId
     * @return 前端轮询判断是否秒杀成功
     */
    @GetMapping("result")
    @ResponseBody
    public ResponseEntity<Long> checkSeckillOrder(@RequestParam("goodsId") Long goodsId) {
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        Long result = seckillService.checkSeckillOrder(goodsId,userInfo.getId());

        if (result == null){
            return ResponseEntity.ok(-1L);
        }

        return ResponseEntity.ok(result);

    }


    /////////////////////////////////// 查询秒杀列表及秒杀的商品信息///////////////////
    /**
     * 查询可以秒的商品列表
     * @return
     */
    @GetMapping("list")
    public @ResponseBody ResponseEntity<List<SeckillGoods>> listSeckillGoods() {
        return ResponseEntity.ok(seckillService.querySecKillList());
    }

    /**
     * 查询可以秒杀的商品详情
     * @return
     */
    @GetMapping("{id}")
    public @ResponseBody ResponseEntity<SeckillGoods> querySeckillGoods(@PathVariable("id") Long id) {
        return ResponseEntity.ok(seckillService.queryGoodsInfoFormCache(id));
    }
}
