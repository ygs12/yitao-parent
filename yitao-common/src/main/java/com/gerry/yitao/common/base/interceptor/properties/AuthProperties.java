package com.gerry.yitao.common.base.interceptor.properties;

import com.gerry.yitao.common.util.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/8 11:56
 * @Description:
 */

@Data
@Slf4j
@ConfigurationProperties(prefix = "yt.jwt")
public class AuthProperties {

    private String pubKeyPath;
    private PublicKey publicKey;
    private String cookieName;

    @PostConstruct
    public void init() {
        try {
            //获取公钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            log.error("初始化公钥失败", e);
            throw new RuntimeException();
        }

    }
}
