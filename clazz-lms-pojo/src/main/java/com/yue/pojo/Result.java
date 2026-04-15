package com.yue.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result：generic response class
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Result {

    // Response Code: 1, success; 0, fail
    private Integer code;
    // tips message: success or fail
    private String msg;
    // data: the data of the response
    private Object data;

    public static Result success() {
        return Result.builder()
                .code(1)
                .msg("success")
                .build();
    }

    public static Result success(Object object) {
        return Result.builder()
                .code(1)
                .msg("success")
                .data(object)
                .build();
    }

    public static Result error(String msg) {
        return Result.builder()
                .code(0)
                .msg(msg)
                .build();
    }
}
