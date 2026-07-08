package com.yue.controller;

import com.yue.pojo.Result;
import com.yue.utils.FileStorage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    private final FileStorage fileStorage;

    public UploadController(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

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
        log.info("File upload：{}", file);

        String key = fileStorage.upload(file.getBytes(), file.getOriginalFilename(), file.getContentType());
        log.info("File upload to AWS S3, key={}", key);
        return Result.success(key);
    }
}
