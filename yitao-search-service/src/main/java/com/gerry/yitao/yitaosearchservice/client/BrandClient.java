package com.gerry.yitao.yitaosearchservice.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gerry.yitao.common.util.RestTemplateUtils;
import com.gerry.yitao.domain.Brand;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/23 18:46
 * @Description:
 */
@Component
public class BrandClient {

    @Value("${client.url:}")
    private String url;

    public Brand queryById(Long id) {
        String requestUrl = url+"api/item/brand/{id}";
        ResponseEntity<Brand> entity = RestTemplateUtils.get(requestUrl,Brand.class,id);

        return entity.getBody();
    }

    public List<Brand> queryBrandsByIds(List<Long> ids) {
        String requestUrl = url+"api/item/brand/list?ids={ids}";
        Map<String, Object> params = new HashMap<>();
        params.put("ids", Joiner.on(",").join(ids));
        ResponseEntity<List> entity = RestTemplateUtils.get(requestUrl,List.class,params);
        List<Brand> brands = JSONObject.parseArray(JSON.toJSONString(entity.getBody()), Brand.class);

        return brands;
    }
}
