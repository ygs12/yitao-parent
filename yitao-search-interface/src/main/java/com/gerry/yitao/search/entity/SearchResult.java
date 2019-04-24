package com.gerry.yitao.search.entity;


import com.gerry.yitao.domain.Brand;
import com.gerry.yitao.domain.Category;
import com.gerry.yitao.entity.PageResult;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/23 18:21
 * @Description:
 */
@Data
public class SearchResult<Goods> extends PageResult<Goods>  implements Serializable {

    private List<Brand> brands;
    private List<Category> categories;
    //规格参数过滤条件
    private List<Map<String, Object>> specs;

    public SearchResult(Long total,
                        Integer totalPage,
                        List<Goods> items,
                        List<Category> categories,
                        List<Brand> brands,
                        List<Map<String, Object>> specs) {
        super(total, totalPage, items);
        this.categories = categories;
        this.brands = brands;
        this.specs = specs;
    }
}
