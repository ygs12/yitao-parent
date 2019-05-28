package com.gerry.yitao.yitaouserservice;

import com.gerry.yitao.domain.SeckillGoods;
import com.gerry.yitao.seckill.service.SeckillService;
import com.gerry.yitao.yitaoseckillservice.YitaoSeckillServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = YitaoSeckillServiceApplication.class)
public class YitaoSecKillApplicationTests {
    @Autowired
    private SeckillService seckillService;

    @Test
    public void testFindSeckillList() {
        List<SeckillGoods> seckillGoods = seckillService.querySecKillList();
        for (SeckillGoods seckillGood : seckillGoods) {
            System.out.println(seckillGood);
        }
    }
}
