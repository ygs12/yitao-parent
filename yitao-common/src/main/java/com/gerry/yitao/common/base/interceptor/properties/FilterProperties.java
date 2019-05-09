package com.gerry.yitao.common.base.interceptor.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/8 11:55
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "yt.filter")
public class FilterProperties {
    private List<String> allowPaths;
}