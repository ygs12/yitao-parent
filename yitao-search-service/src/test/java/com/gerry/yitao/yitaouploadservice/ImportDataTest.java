package com.gerry.yitao.yitaouploadservice;

import com.gerry.yitao.domain.Spu;
import com.gerry.yitao.entity.PageResult;
import com.gerry.yitao.search.entity.Goods;
import com.gerry.yitao.search.service.SearchService;
import com.gerry.yitao.yitaosearchservice.client.GoodsClient;
import com.gerry.yitao.yitaosearchservice.respository.GoodsRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/24 16:17
 * @Description:
 */
public class ImportDataTest extends YitaoSearchApplicationTests {
    @Autowired
    private GoodsClient goodsClient;

    @Autowired(required = false)
    private SearchService searchService;

    @Autowired
    private GoodsRepository repository;

    @Test
    public void loadData() {
        int page = 1;
        int size = 0;
        int rows = 100;
        do {
            // 上架商品
            PageResult<Spu> result = goodsClient.querySpuByPage(page, rows, null, true);
            ArrayList<Goods> goodList = new ArrayList<>();
            List<Spu> spus = result.getRows();
            size = spus.size();
            for (Spu spu : spus) {
                try {
                    Goods g = searchService.buildGoods(spu);
                    goodList.add(g);

                } catch (Exception e) {
                    break;
                }
            }
            this.repository.saveAll(goodList);
            page++;
        } while (size == 100);
    }
}
