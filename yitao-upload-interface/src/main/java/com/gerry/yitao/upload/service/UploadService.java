package com.gerry.yitao.upload.service;

import com.gerry.yitao.upload.bo.UploadBo;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/17 18:12
 * @Description: 上传图片的服务接口
 */
public interface UploadService {
    String uploadImage(UploadBo uploadBo);
    Integer deleteImage(String imagePath);
}
