package com.gerry.yitao.yitaoorderservice.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gerry.yitao.common.util.RestTemplateUtils;
import com.gerry.yitao.domain.Sku;
import com.gerry.yitao.dto.CartDto;
import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;

    @Value("${client.url:}")
    private String url;

    public List<Sku> querySkuBySpuId(Long spuId) {
        String requestUrl = url+"api/item/sku/list?id={id}";
        ResponseEntity<List> entity = RestTemplateUtils.get(requestUrl,List.class, spuId);
        List<Sku> skus = JSONObject.parseArray(JSON.toJSONString(entity.getBody()), Sku.class);

        return skus;
    }

    public List<Sku> querySkusByIds(List<Long> ids) {
        String requestUrl = url+"api/item/sku/list/ids?ids={ids}";
        Map<String, Object> params = new HashMap<>();
        params.put("ids", Joiner.on(",").join(ids));
        ResponseEntity<List> entity = RestTemplateUtils.get(requestUrl,List.class, params);
        List<Sku> skus = JSONObject.parseArray(JSON.toJSONString(entity.getBody()), Sku.class);

        return skus;
    }

    /**
     * 减库存的远程方法调用
     * @param cartDtos
     */
    public void decreaseStock(List<CartDto> cartDtos) {
        String requestUrl = url+"api/item/stock/decrease";
        HttpHeaders headers = new HttpHeaders();
        //定义请求参数类型，这里用json所以是MediaType.APPLICATION_JSON
        headers.setContentType(MediaType.APPLICATION_JSON);
        //RestTemplate带参传的时候要用HttpEntity<?>对象传递
        HttpEntity<List<CartDto>> request = new HttpEntity<List<CartDto>>(cartDtos, headers);
        RestTemplateUtils.post(requestUrl, request,Void.class);
    }
}
