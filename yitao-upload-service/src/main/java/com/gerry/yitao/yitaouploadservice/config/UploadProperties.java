package com.gerry.yitao.yitaouploadservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/17 18:19
 * @Description:
 */
@Data
@ConfigurationProperties(prefix = "yitao.upload")
@Component
public class UploadProperties {

    private String baseUrl;
    private List<String> allowTypes;
}
