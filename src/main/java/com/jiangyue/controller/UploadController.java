package com.jiangyue.controller;

import com.jiangyue.pojo.Result;
import com.jiangyue.utils.AliyunOSSOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
public class UploadController {

    /**
     * 本地存储文件
     * @param name
     * @param age
     * @param file
     * @return
     * @throws IOException
     */
//    @PostMapping("/upload")
//    public Result upload(String name, Integer age, MultipartFile file) throws IOException {
//        log.info("接收到的参数：{}, {}, {}", name, age, file);
//        // 获取原始文件名
//        String oringinalFilename =  file.getOriginalFilename();
//
//        // 新的文件名
//        String extension = oringinalFilename.substring(oringinalFilename.lastIndexOf("."));
//        String newFileName = UUID.randomUUID().toString() + extension;
//        // 保存文件
//        file.transferTo(new File("/Users/jiangyue/temp/" + newFileName));
//        return Result.success();
//    }
    @Autowired
    private AliyunOSSOperator aliyunOSSOperator;

    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传：{}", file);
        // 将文件交给 OSS 存储管理
        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
        log.info("文件上传OSS，url：{}", url);
        return Result.success(url);
    }
}
