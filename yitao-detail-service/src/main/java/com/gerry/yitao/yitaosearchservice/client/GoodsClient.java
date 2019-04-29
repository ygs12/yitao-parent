package com.gerry.yitao.yitaosearchservice.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gerry.yitao.common.util.RestTemplateUtils;
import com.gerry.yitao.domain.Sku;
import com.gerry.yitao.domain.Spu;
import com.gerry.yitao.domain.SpuDetail;
import com.gerry.yitao.entity.PageResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/23 19:30
 * @Description:
 */
@Component
public class GoodsClient {
    @Value("${client.url:}")
    private String url;

    public List<Sku> querySkuBySpuId(Long spuId) {
        String requestUrl = url+"api/item/sku/list?id={id}";
        ResponseEntity<List> entity = RestTemplateUtils.get(requestUrl,List.class, spuId);
        List<Sku> skus = JSONObject.parseArray(JSON.toJSONString(entity.getBody()), Sku.class);

        return skus;
    }

    public SpuDetail querySpuDetailById(Long spuId) {
        String requestUrl = url+"api/item/spu/detail/{spuId}";
        ResponseEntity<SpuDetail> entity = RestTemplateUtils.get(requestUrl,SpuDetail.class, spuId);

        return entity.getBody();
    }

    public Spu querySpuBySpuId(Long id) {
        String requestUrl = url+"api/item/spu/{id}";
        ResponseEntity<Spu> entity = RestTemplateUtils.get(requestUrl,Spu.class, id);

        return entity.getBody();
    }

    public PageResult<Spu> querySpuByPage(int page, int rows, String key, Boolean saleable) {
        String requestUrl = url + "api/item/spu/page?page={page}&rows={rows}&key={key}&saleable={saleable}";
        Map<String, Object> params = new HashMap<>();
        params.put("page", page);
        params.put("rows", rows);
        params.put("key", key);
        params.put("saleable", saleable);
        ResponseEntity<PageResult> entity = RestTemplateUtils.get(requestUrl, PageResult.class, params);
        PageResult body = entity.getBody();
        List<Spu> items = JSONObject.parseArray(JSON.toJSONString(body.getRows()), Spu.class);
        body.setRows(items);

        return body;
    }
}
