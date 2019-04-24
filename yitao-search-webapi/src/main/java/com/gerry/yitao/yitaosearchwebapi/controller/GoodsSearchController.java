package com.gerry.yitao.yitaosearchwebapi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gerry.yitao.search.entity.Goods;
import com.gerry.yitao.search.entity.SearchRequest;
import com.gerry.yitao.search.entity.SearchResult;
import com.gerry.yitao.search.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/24 16:13
 * @Description:
 */
@RestController
@RequestMapping("search")
public class GoodsSearchController {
    @Reference(check = false,timeout = 3000)
    private SearchService searchService;

    @PostMapping("page")
    public ResponseEntity<SearchResult<Goods>> search(@RequestBody SearchRequest searchRequest) {
        return ResponseEntity.ok(searchService.search(searchRequest));
    }
}
