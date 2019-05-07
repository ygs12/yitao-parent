package com.gerry.yitao.yitaosmsservice.config;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/6 18:21
 * @Description:
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
@ConfigurationProperties(prefix = "yt.sms")
@Data
public class SmsProperties {
    private String accessKeyId;
    private String accessKeySecret;

}
