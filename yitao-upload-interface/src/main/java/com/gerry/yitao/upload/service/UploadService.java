package com.gerry.yitao.upload.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/17 18:12
 * @Description: 上传图片的服务接口
 */
public interface UploadService {
    String uploadImage(MultipartFile file);
}
