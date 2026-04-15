package com.yue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Student degree statistics VO (view object)
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class StudentDegreeVO {
    private String name;
    private Integer value;
}
