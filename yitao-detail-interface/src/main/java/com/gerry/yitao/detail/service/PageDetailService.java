package com.gerry.yitao.detail.service;

import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/29 18:11
 * @Description:
 */
public interface PageDetailService {
    Map<String, Object> loadModel(Long spuId);
    void createHtml(Long spuId);
    void deleteHtml(Long id);
}
