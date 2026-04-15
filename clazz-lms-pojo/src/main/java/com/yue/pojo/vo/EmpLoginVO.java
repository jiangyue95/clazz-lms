package com.yue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * employee login view object
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpLoginVO {
    private Integer id;
    private String username;
    private String name;
    private String token;
}
