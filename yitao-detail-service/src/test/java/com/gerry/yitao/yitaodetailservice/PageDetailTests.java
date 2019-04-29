package com.gerry.yitao.yitaodetailservice;

import com.alibaba.fastjson.JSON;
import com.gerry.yitao.detail.service.PageDetailService;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/29 20:46
 * @Description:
 */
public class PageDetailTests extends YitaoPageDetailApplicationTests {

    @Resource
    private PageDetailService pageDetailService;

    @Test
    public void test1() {
        Map<String, Object> map = pageDetailService.loadModel(51L);
        System.out.println(JSON.toJSONString(map,true));
    }

    @Test
    public void createHtml() {
        // 当你首次访问的商品详情页时。
        // 1、动态渲染
        // 2、判断指定目录下面是否存在了静态页面，如果存在直接访问，如果不存在通过，重新访问生成对应静态页面
        // if (!f -name $) {
              //  root html;
        //}
           // proxy_pass = "192.168.3.101:10040";

        pageDetailService.createHtml(57L);
    }


}
