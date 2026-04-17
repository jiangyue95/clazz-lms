package com.yue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Employee gender vo (view object)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmpGenderVO {
    private String name;
    private Long value;
}
