package com.gerry.yitao.yitaouploadservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.gerry.yitao.common.exception.ServiceException;
import com.gerry.yitao.common.upload.FastdfsClient;
import com.gerry.yitao.upload.bo.UploadBo;
import com.gerry.yitao.upload.service.UploadService;
import com.gerry.yitao.yitaouploadservice.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/17 18:19
 * @Description:
 */
@Service(timeout = 40000)
@Component
@Slf4j
public class UploadServiceImpl implements UploadService {
    @Autowired
    private UploadProperties prop;
    @Autowired
    private FastdfsClient storageClient;

    @Override
    public String uploadImage(UploadBo uploadBo) {

        if (!prop.getAllowTypes().contains(uploadBo.getContentType().trim())) {
            throw new ServiceException("文件的类型不正确");
        }

        //检验文件内容
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(uploadBo.getBytes()));
            if (image == null) {
                log.info("【文件上传】上传文件格式错误");
                throw new ServiceException("上传文件的格式不正确");
            }
        } catch (IOException e) {
            log.info("【文件上传】文件上传失败", e);
            throw new ServiceException("文件上传失败");
        }

        //保存图片
        try {
            String storePath = storageClient.uploadFile(uploadBo.getBytes(), uploadBo.getFileName());
            //返回保存图片的完整url
            return prop.getBaseUrl() + storePath;
        } catch (Exception e) {
            log.info("【文件上传】图片上传异常", e);
            throw new ServiceException("图片上传异常");
        }
    }

    @Override
    public Integer deleteImage(String imagePath) {
        Integer result = storageClient.delete_file(imagePath);

        if (result.intValue() == -1) {
            throw new ServiceException("删除图片失败");
        }

        return result;
    }
}
