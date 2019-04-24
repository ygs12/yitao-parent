package com.gerry.yitao.search.service;

import com.gerry.yitao.domain.Category;
import com.gerry.yitao.domain.Spu;
import com.gerry.yitao.search.entity.Goods;
import com.gerry.yitao.search.entity.SearchRequest;
import com.gerry.yitao.search.entity.SearchResult;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;

import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/23 18:21
 * @Description:
 */
public interface SearchService {
    Goods buildGoods(Spu spu);
    SearchResult<Goods> search(SearchRequest searchRequest);
    List<Category> handleCategoryAgg(LongTerms terms);
    void insertOrUpdate(Long id);
    void delete(Long id);
}
