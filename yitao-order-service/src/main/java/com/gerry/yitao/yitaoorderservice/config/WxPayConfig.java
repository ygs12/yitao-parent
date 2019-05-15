package com.gerry.yitao.yitaoorderservice.config;

import com.github.wxpay.sdk.WXPayConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/15 15:24
 * @Description:
 */
@Data
@Slf4j
public class WxPayConfig implements WXPayConfig {

    private String appID;  //公众账号ID

    private String mchID;  //商户号

    private String key;  //生成签名的密钥

    private String certPath; // API证书绝对路径

    private int httpConnectTimeoutMs=8000;   //连接超时时间

    private int httpReadTimeoutMs=10000;  //读取超时时间

    private String notifyUrl;// 下单通知回调地址


    /**
     * 获取商户证书内容
     *
     * @return 商户证书内容
     */
    @Override
    public InputStream getCertStream()  {
        File certFile = new File(certPath);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(certFile);
        } catch (FileNotFoundException e) {
            log.error("cert file not found, path={}, exception is:{}", certPath, e);
        }
        return inputStream;
    }
}