package com.gerry.yitao.yitaosearchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.gerry.yitao.common.exception.ServiceException;
import com.gerry.yitao.detail.service.PageDetailService;
import com.gerry.yitao.domain.*;
import com.gerry.yitao.yitaosearchservice.client.BrandClient;
import com.gerry.yitao.yitaosearchservice.client.CategoryClient;
import com.gerry.yitao.yitaosearchservice.client.GoodsClient;
import com.gerry.yitao.yitaosearchservice.client.SpecClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/29 18:28
 * @Description:
 */
@Service(timeout = 40000)
@Slf4j
public class PageDetailServiceImpl implements PageDetailService {
    @Autowired
    private BrandClient brandClient;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecClient specClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${yt.page.path}")
    private String dest;

    public Map<String, Object> loadModel(Long spuId) {
        Map<String, Object> model = new HashMap<>();
        Spu spu = goodsClient.querySpuBySpuId(spuId);

        //未上架，则不应该查询到商品详情信息，抛出异常
        if (!spu.getSaleable()) {
            throw new ServiceException("查询了未上架的商品");
        }

        SpuDetail detail = spu.getSpuDetail();
        List<Sku> skus = spu.getSkus();
        Brand brand = brandClient.queryById(spu.getBrandId());
        //查询三级分类
        List<Category> categories = categoryClient.queryByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));

        List<SpecGroup> specs = specClient.querySpecsByCid(spu.getCid3());

        model.put("brand", brand);
        model.put("categories", categories);
        model.put("spu", spu);
        model.put("skus", skus);
        model.put("detail", detail);
        model.put("specs", specs);
        return model;
    }

    public  void createHtml(Long spuId) {
        Context context = new Context();
        Map<String, Object> map = loadModel(spuId);
        context.setVariables(map);

        File file = new File(this.dest, spuId + ".html");
        //如果页面存在，先删除，后进行创建静态页
        if (file.exists()) {
            file.delete();
        }
        try (PrintWriter writer = new PrintWriter(file, "utf-8")) {
            templateEngine.process("item", context, writer);
        } catch (Exception e) {
            log.error("【静态页服务】生成静态页面异常", e);
        }
    }

    /**
     * 异步处理
     * @param id
     */
    public void deleteHtml(Long id) {
        File file = new File(this.dest + id + ".html");
        if (file.exists()) {
            boolean flag = file.delete();
            if (!flag) {
                log.error("删除静态页面失败");
            }
        }
    }
}
