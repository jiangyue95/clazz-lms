package com.yue.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result：generic response class
 *
 * @param <T> the type of the data
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    // Response Code: 1, success; 0, fail
    private Integer code;
    // tips message: success or fail
    private String msg;
    // data: the data of the response
    private T data;

    public static <T> Result<T> success() {
        return Result.<T>builder()
                .code(1)
                .msg("success")
                .build();
    }

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(1)
                .msg("success")
                .data(data)
                .build();
    }

    public static <T> Result<T> error(String msg) {
        return Result.<T>builder()
                .code(0)
                .msg(msg)
                .build();
    }
}
