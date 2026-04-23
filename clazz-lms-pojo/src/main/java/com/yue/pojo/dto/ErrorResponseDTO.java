package com.yue.pojo.dto;

import java.time.LocalDateTime;

/**
 *
 * @param errorCode
 * @param message
 * @param timestamp
 * @param path
 */
public record ErrorResponseDTO(
        String errorCode,
        String message,
        LocalDateTime timestamp,
        String path
) {
}
