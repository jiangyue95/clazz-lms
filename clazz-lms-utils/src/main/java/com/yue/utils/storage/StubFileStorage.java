package com.yue.utils.storage;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
public class StubFileStorage implements FileStorage {

    public StubFileStorage() {
        log.warn("Using StubFileStorage: files are NOT persisted and the returned "
                + "URLs are not usable. Intended for tests and for running without "
                + "AWS configuration. Set app.storage.type=s3 for real storage.");
    }

    @Override
    public String upload(byte[] content, String originalFilename, String contentType) {
        String dir = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        int dot = originalFilename.lastIndexOf(".");
        String extension = dot < 0 ? "" : originalFilename.substring(dot);
        return dir + '/' + UUID.randomUUID() + extension;
    }

    @Override
    public String generatePresignedUrl(String key) {
        return "stub://not-a-real-url/" + key;
    }
}
