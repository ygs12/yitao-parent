package com.gerry.yitao.upload.bo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/18 21:15
 * @Description:
 */
@Data
public class UploadBo implements Serializable {
    private static final long serialVersionUID = 1L;
    private byte[] bytes;
    private String contentType;
    private String fileName;
}
