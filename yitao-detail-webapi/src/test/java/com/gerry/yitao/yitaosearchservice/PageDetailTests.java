package com.gerry.yitao.yitaosearchservice;

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
public class PageDetailTests extends YitaoDetailServiceApplicationTests {

    @Resource
    private PageDetailService pageDetailService;

    @Test
    public void test1() {
        Map<String, Object> map = pageDetailService.loadModel(51L);
        System.out.println(JSON.toJSONString(map,true));
    }
}
