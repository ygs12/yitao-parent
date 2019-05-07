package com.gerry.yitao.yitaosmsservice.utils;

import com.gerry.yitao.yitaosmsservice.config.SmsProperties;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/5/6 18:19
 * @Description: 短信服务工具类
 */
@Component
@EnableConfigurationProperties(SmsProperties.class)
public class SmsUtil {

    @Autowired(required = false)
    private SmsProperties smsProperties;

    private static String URL = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";

    public void sendSms(String phone, String code) {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(URL);

        client.getParams().setContentCharset("GBK");
        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=GBK");

        String content = new String("您的验证码是："+code+"。请不要把验证码泄露给其他人。");

        NameValuePair[] data = {//提交短信
                new NameValuePair("account", smsProperties.getAccessKeyId().trim()), //查看用户名是登录用户中心->验证码短信->产品总览->APIID
                new NameValuePair("password", smsProperties.getAccessKeySecret().trim()),  //查看密码请登录用户中心->验证码短信->产品总览->APIKEY
                new NameValuePair("mobile", phone),
                new NameValuePair("content", content),
        };
        method.setRequestBody(data);

        try {
            client.executeMethod(method);
            String SubmitResult =method.getResponseBodyAsString();

            Document doc = DocumentHelper.parseText(SubmitResult);
            Element root = doc.getRootElement();

            String statusCode = root.elementText("code");
            String msg = root.elementText("msg");
            System.out.println(msg);
            if("2".equals(statusCode)){
                System.out.println("短信提交成功");
            }

        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } finally{
            // Release connection
            method.releaseConnection();
        }
    }
}
