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
 * <p>Uploads a file to AWS S3 via the {@link  FileStorage} abstraction and
 * return the stored object key. The key is stable and safe to persist;
 * a short-lived presigned URL can be generated from it on read.
 */
@Tag(
        name = "File Upload",
        description = "File upload endpoints backed by AWS S3"
)
@Slf4j
@RestController
public class UploadController {

    private final FileStorage fileStorage;

    public UploadController(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
    }

    @Operation(
            summary = "Upload a file to AWS S3",
            description = "Uploads a file to AWS S3 and returns the stored object key.",
            operationId = "uploadFile"
    )
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("File upload:{}", file.getOriginalFilename());

        String key = fileStorage.upload(file.getBytes(), file.getOriginalFilename(), file.getContentType());
        log.info("File upload to AWS S3, key={}", key);
        return Result.success(key);
    }
}
