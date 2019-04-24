package com.gerry.yitao.yitaouploadservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gerry.yitao.common.util.RestTemplateUtils;
import com.gerry.yitao.domain.*;
import com.gerry.yitao.entity.PageResult;
import com.gerry.yitao.search.entity.Goods;
import com.gerry.yitao.search.service.SearchService;
import com.gerry.yitao.yitaosearchservice.client.BrandClient;
import com.gerry.yitao.yitaosearchservice.client.GoodsClient;
import com.gerry.yitao.yitaosearchservice.client.SpecClient;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/23 19:00
 * @Description:
 */
public class RestTest extends YitaoSearchApplicationTests {

    @Autowired
    private BrandClient brandClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private SpecClient specClient;

    @Autowired(required = false)
    private SearchService searchService;

    @Test // 测试查询单个对象
    public void testBrand() throws Exception {
        Brand brand = brandClient.queryById(3539L);
        System.out.println(brand);

    }

    @Test // 测试查询集合
    public void testBrandList() throws Exception {
        List<Brand> brands = brandClient.queryBrandsByIds(Lists.newArrayList(6522L, 7174L, 7203L));
        System.out.println(brands);
    }

    @Test
    public void test11() {
        Spu spu = goodsClient.querySpuBySpuId(195L);
        Goods goods = searchService.buildGoods(spu);
        System.out.println(goods);
    }

    @Test
    public void testGoodsPage() {
        PageResult<Spu> spuPageResult = goodsClient.querySpuByPage(1, 20, null, true);
        System.out.println(spuPageResult);
    }

    @Test
    public void te1() {
        System.out.println(searchService);
        List<Sku> skus = goodsClient.querySkuBySpuId(11L);
        System.out.println(skus);
    }

    @Test
    public void te2() {
        SpuDetail spuDetail = goodsClient.querySpuDetailById(11L);
        System.out.println(spuDetail);
    }

    /**
     * @throws Exception
     */
    @Test
    public void test3() throws Exception {
        List<SpecParam> specParams = specClient.querySpecParams(2,76L,false,true);
        System.out.println(specParams);
    }
    /////////////////////////////////////////////////////////////
    /**
     * 测试HTTPS请求访问博客园
     */
    @Test
    public void test() {
        String url = "https://www.cnblogs.com/{id}/p/{pageName}.html";
        String id = "jonban";
        List<String> pages = new ArrayList<>();
        pages.add("rest");
        pages.add("jsoup");
        pages.add("sms");
        pages.add("rememberMe");
        pages.add("properties");
        pages.add("async");

        for (String pageName : pages) {
            ResponseEntity<String> entity = RestTemplateUtils.get(url, String.class, id, pageName);
            System.out.println(entity.getStatusCode());
            System.out.println(entity.getBody());
        }
    }

    @Test
    public void test1() {
        String url = "http://api.yitao.com/api/item/brand/list?ids={ids}";
        Map<String, Object> params = new HashMap<>();

        params.put("ids", Joiner.on(",").join(Arrays.asList(6522,7203)));

        /*List forObject = new RestTemplate().getForObject(url, List.class, params);
        System.out.println(forObject);*/

        ResponseEntity<List> entity = RestTemplateUtils.get(url,List.class,params);
        System.out.println(entity.getStatusCode());
        System.out.println(entity.getBody());
        List<Brand> brands = JSONObject.parseArray(JSON.toJSONString(entity.getBody()), Brand.class);
        System.out.println(brands);
    }

    @Test
    public void test2() {
        String url = "http://api.yitao.com/api/item/brand/{id}";

        /*List forObject = new RestTemplate().getForObject(url, List.class, params);
        System.out.println(forObject);*/

        ResponseEntity<Brand> entity = RestTemplateUtils.get(url,Brand.class,7817);
        System.out.println(entity.getStatusCode());
        System.out.println(entity.getBody());
    }

    /**
     * 测试带请求头参数Headers的GET请求，POST类似
     */
    @Test
    public void testHeaders() {
        String url = "http://127.0.0.1:8080/test/Logan?age=16";
        Map<String, String> headers = new HashMap<>();
        headers.put("appId", "Basic MyAppId");
        ResponseEntity<String> entity = RestTemplateUtils.get(url, headers, String.class);
        System.out.println(entity.getStatusCode());
        System.out.println(entity.getBody());
    }

    /**
     * 测试普通表单参数的POST请求
     */
    @Test
    public void sayHello() {
        String url = "http://127.0.0.1:8080/test/sayHello";
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("name", "Logan");
        requestBody.add("age", 12);
        ResponseEntity<JSONObject> response = RestTemplateUtils.post(url, requestBody, JSONObject.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }

    /**
     * 测试JSON格式请求体Body方式POST请求
     */
    @Test
    public void sayHelloBody() {
        String url = "http://127.0.0.1:8080/test/sayHelloBody";
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "Logan");
        requestBody.put("age", 16);
        ResponseEntity<JSONObject> response = RestTemplateUtils.post(url, requestBody, JSONObject.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }

    /**
     * 测试上传文件
     */
    @Test
    public void uploadFile() {
        String url = "http://127.0.0.1:8080/test/uploadFile";
        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("uploadPath", "G:\\Temp\\Test");
        requestBody.add("file", new FileSystemResource("D:\\Java\\JavaStyle.xml"));
        requestBody.add("file2", new FileSystemResource("D:\\Java\\jad.exe"));

        ResponseEntity<JSONObject> response = RestTemplateUtils.post(url, requestBody, JSONObject.class);

        System.out.println(response.getStatusCode());
        System.out.println(response.getBody());
    }

    @Test
    public void downloadFile() {
        try {
            String url = "http://127.0.0.1:8080/test/downloadFile?filePath={filePath}&fileName={fileName}";

            String filePath = "D:\\Java";
            String fileName = "JavaStyle.xml";

            ResponseEntity<byte[]> response = RestTemplateUtils.get(url, byte[].class, filePath, fileName);
            System.out.println(response.getStatusCode());
            System.out.println(response.getHeaders().getContentType());

            // 如果返回时文本内容，则直接输出
            if ("text/html;charset=UTF-8".equals(response.getHeaders().getContentType().toString())) {
                System.out.println(new String(response.getBody(), "UTF-8"));
            }

            // 输出响应内容到本地文件
            else {

                File file = new File("D:\\Temp\\Test", fileName);
                if (HttpStatus.OK.equals(response.getStatusCode())) {
                    FileUtils.writeByteArrayToFile(file, response.getBody());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
