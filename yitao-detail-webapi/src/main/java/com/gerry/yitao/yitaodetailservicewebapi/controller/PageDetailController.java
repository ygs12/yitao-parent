package com.gerry.yitao.yitaodetailservicewebapi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gerry.yitao.detail.service.PageDetailService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/29 18:49
 * @Description:
 */
@Controller
public class PageDetailController {
    @Reference(timeout = 40000)
    private PageDetailService detailService;

    @GetMapping("item/{id}.html")
    public String toItemPage(@PathVariable("id") Long spuId, Model model) {
        Map<String, Object> attributes = detailService.loadModel(spuId);
        model.addAllAttributes(attributes);
        // 同步
        //detailService.createHtml(spuId);
        // 异步生成静态详情页
        detailService.asyncExecute(spuId);

        return "item";
    }
}
