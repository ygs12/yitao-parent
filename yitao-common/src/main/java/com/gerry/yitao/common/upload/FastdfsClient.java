package com.gerry.yitao.common.upload;

import com.gerry.yitao.common.exception.ResourceNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/20 00:47
 * @Description:
 */
public class FastdfsClient {

    private static StorageClient1 storageClient1 = null;

    // 初始化FastDFS Client
    public FastdfsClient(String config) {
        if (config.contains("classpath:")) {
            config = config.replace("classpath:", this.getClass().getResource("/").getPath());
        }
        try {
            ClientGlobal.init(config);
            TrackerClient trackerClient = new TrackerClient(ClientGlobal.g_tracker_group);
            TrackerServer trackerServer = trackerClient.getConnection();
            if (trackerServer == null) {
                throw new IllegalStateException("getConnection return null");
            }
            //给dfs发送一个消息
            ProtoCommon.activeTest(trackerServer.getSocket());

            StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
            if (storageServer == null) {
                throw new IllegalStateException("getStoreStorage return null");
            }
            //给dfs发送一个消息
            ProtoCommon.activeTest(storageServer.getSocket());

            storageClient1 = new StorageClient1(trackerServer,storageServer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     * @param bytes 文件字节对象
     * @param fileName 文件名
     * @return
     */
    public String uploadFile(byte[] bytes, String fileName) {
        return uploadFile(bytes,fileName,null);
    }

    /**
     *
     * @param storagePath  文件的全部路径 如：group1/M00/00/00/wKgRsVjtwpSAXGwkAAAweEAzRjw471.jpg
     * @return -1失败,0成功
     * @throws IOException
     * @throws Exception
     */
    public Integer delete_file(String storagePath){
        int result=-1;
        try {
            result = storageClient1.delete_file1(storagePath);
        } catch (Exception e) {
            throw new ResourceNotFoundException("删除的图片路径不存在");
        }
        return  result;
    }

    /**
     * 上传文件
     * @param bytes 文件字节对象
     * @param fileName 文件名
     * @param metaList 文件元数据
     * @return
     */
    private String uploadFile(byte[] bytes, String fileName, Map<String,String> metaList) {
        try {
            NameValuePair[] nameValuePairs = null;
            if (metaList != null) {
                nameValuePairs = new NameValuePair[metaList.size()];
                int index = 0;
                for (Iterator<Map.Entry<String,String>> iterator = metaList.entrySet().iterator(); iterator.hasNext();) {
                    Map.Entry<String,String> entry = iterator.next();
                    String name = entry.getKey();
                    String value = entry.getValue();
                    nameValuePairs[index++] = new NameValuePair(name,value);
                }
            }
            String extensionName = StringUtils.substringAfterLast(fileName, ".");
            return storageClient1.upload_file1(bytes, extensionName,nameValuePairs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
