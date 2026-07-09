package com.yue.utils;

public interface FileStorage {

    /**
     * Uploads files content and returns the stored object key.
     * The key is stable and is what callers should persist.
     * @return
     */
    String upload(byte[] content, String originalFilename, String contentType);

    /**
     * Generates a short-lived, presigned URL for reading the object at {@code key}.
     * The URL expires, so it must be generated on demand and never persisted.
     */
    String generatePresignedUrl(String key);
}
