package com.gerry.yitao.search.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/23 18:21
 * @Description:
 */
@Data
public class SearchRequest implements Serializable {

    private static final Integer DEFAULT_PAGE = 1;
    private static final Integer DEFAULT_SIZE = 20;
    private String key;
    private Integer page;
    //排序字段
    private String sortBy;
    //是否降序
    private Boolean descending;
    //过滤字段
    private Map<String, String> filter;

    public Integer getPage() {
        if (page == null) {
            return DEFAULT_PAGE;
        }
        // 获取页码时做一些校验，不能小于1
        return Math.max(DEFAULT_PAGE, page);
    }

    public Integer getSize() {
        return DEFAULT_SIZE;
    }

}
