package com.gerry.yitao.yitaouploadwebapi.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gerry.yitao.upload.bo.UploadBo;
import com.gerry.yitao.upload.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @ProjectName: yitao-parent
 * @Auther: GERRY
 * @Date: 2019/4/17 18:31
 * @Description:
 */
@RestController
@RequestMapping("api/upload")
public class UploadController {

    @Reference(check = false, timeout = 40000)
    private UploadService uploadService;


    /**
     * 图片的上传
     *
     * @param file
     * @return
     */
    @PostMapping("image")
    public ResponseEntity<String> uploadImage(UploadBo uploadBo, @RequestParam("file") MultipartFile file) throws IOException {
        uploadBo.setBytes(file.getBytes());
        uploadBo.setContentType(file.getContentType());
        uploadBo.setFileName(file.getOriginalFilename());

        return ResponseEntity.ok(uploadService.uploadImage(uploadBo));
    }

    /**
     * 删除图片
     *
     * @param path
     * @return
     */
    @GetMapping("delete")
    public ResponseEntity<String> delete(@RequestParam("path") String path) throws IOException {

        return ResponseEntity.ok(uploadService.deleteImage(path).toString());
    }
}