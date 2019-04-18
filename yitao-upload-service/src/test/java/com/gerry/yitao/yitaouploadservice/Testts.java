package com.gerry.yitao.yitaouploadservice;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/17 19:03
 * @Description:
 */
public class Testts extends YitaoSellerServiceApplicationTests {
    @Autowired
    private FastFileStorageClient storageClient;

    @Test
    public void test1() throws Exception{
        FileInputStream fileInputStream = new FileInputStream("C:\\Users\\GERRY\\Pictures\\Saved Pictures\\tt.jpg");
        System.out.println(fileInputStream);
        StorePath jpg = storageClient.uploadFile(fileInputStream, fileInputStream.available(), "jpg", null);
        System.out.println(jpg.getFullPath());
    }

}
