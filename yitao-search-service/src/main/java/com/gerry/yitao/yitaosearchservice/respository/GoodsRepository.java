package com.gerry.yitao.yitaosearchservice.respository;

import com.gerry.yitao.search.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/23 18:23
 * @Description:
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
