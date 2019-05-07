package com.gerry.yitao.yitaocartservice.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gerry.yitao.common.util.RestTemplateUtils;
import com.gerry.yitao.domain.Category;
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
 * @Date: 2019/4/23 19:31
 * @Description:
 */
@Component
public class CategoryClient {
    @Value("${client.url:}")
    private String url;

    public List<Category> queryByIds(List<Long> ids) {
        String requestUrl = url+"api/item/category/list/ids?ids={ids}";
        Map<String, Object> params = new HashMap<>();
        params.put("ids", Joiner.on(",").join(ids));
        ResponseEntity<List> entity = RestTemplateUtils.get(requestUrl,List.class,params);
        List<Category> categories = JSONObject.parseArray(JSON.toJSONString(entity.getBody()), Category.class);
        return categories;
    }
}
