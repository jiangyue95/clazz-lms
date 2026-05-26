package com.yue.controller;

import com.yue.pojo.Result;
import com.yue.utils.AliyunOSSOperator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * File upload REST controller.
 *
 * <p><b>Status: temporarily out of service.</b> The Aliyun OSS credentials
 * backing this controller have expired, and migration to AWS S3 is planned
 * in a follow-up PR. This controller is documented here for completeness and
 * marked {@code deprecated} in the OpenAPI spec so consumers see a clear
 * warning in Swagger UI.
 */
@Tag(
        name = "File Upload",
        description = "File upload endpoints (currently out of service - see notes)"
)
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

    @Operation(
            summary = "Upload a file to cloud storage",
            description = "**Temporarily out of service.** This endpoint was " +
                    "backed by Aliyun OSS, whose credentials have expired. " +
                    "Migration to AWS S3 is planned. Calls to this endpoint " +
                    "will currently fail. Do not build new integrations " +
                    "against this endpoint until migration is complete.",
            operationId = "uploadFile",
            deprecated = true


    )
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("文件上传：{}", file);
        // 将文件交给 OSS 存储管理
        String url = aliyunOSSOperator.upload(file.getBytes(), file.getOriginalFilename());
        log.info("文件上传OSS，url：{}", url);
        return Result.success(url);
    }
}
