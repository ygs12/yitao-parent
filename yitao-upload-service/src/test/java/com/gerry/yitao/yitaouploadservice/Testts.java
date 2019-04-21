package com.gerry.yitao.yitaouploadservice;

import com.gerry.yitao.common.upload.FastdfsClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/17 19:03
 * @Description:
 */
public class Testts extends YitaoSellerServiceApplicationTests {
    @Autowired
    private FastdfsClient storageClient;

    @Test
    public void test1() throws Exception{
        File file = new File("D:\\hr.jpg");
        String jpg = storageClient.uploadFile(getBytes(file.getCanonicalPath()), file.getName());
        System.out.println(jpg);
    }

    private byte[] getBytes(String filePath){
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

}
