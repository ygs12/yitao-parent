package com.gerry.yitao.yitaocartservice.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gerry.yitao.common.util.RestTemplateUtils;
import com.gerry.yitao.domain.SpecGroup;
import com.gerry.yitao.domain.SpecParam;
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
public class SpecClient {
    @Value("${client.url:}")
    private String url;

    /**
     *  @RequestParam(value = "gid", required = false) Long gid,
    *   @RequestParam(value = "cid", required = false) Long cid,
    *   @RequestParam(value = "searching", required = false) Boolean searching,
    *   @RequestParam(value = "generic", required = false) Boolean generic
     * @return
     */
    public List<SpecParam> querySpecParams(Object gid, Long cid, Boolean searching, Boolean generic) {
        String requestUrl = url+"api/item/spec/params?gid={gid}&cid={cid}&searching={searching}&generic={generic}";
        Map<String, Object> params = new HashMap<>();
        params.put("gid", gid);
        params.put("cid", cid);
        params.put("searching", searching);
        params.put("generic", generic);
        ResponseEntity<List> entity = RestTemplateUtils.get(requestUrl,List.class,params);
        List<SpecParam> specParams = JSONObject.parseArray(JSON.toJSONString(entity.getBody()), SpecParam.class);

        return specParams;
    }

    public List<SpecGroup> querySpecsByCid(Long cid3) {
        String requestUrl = url+"api/item/spec/{cid}";
        ResponseEntity<List> entity = RestTemplateUtils.get(requestUrl,List.class,cid3);
        List<SpecGroup> specGroups = JSONObject.parseArray(JSON.toJSONString(entity.getBody()), SpecGroup.class);

        return specGroups;
    }
}
