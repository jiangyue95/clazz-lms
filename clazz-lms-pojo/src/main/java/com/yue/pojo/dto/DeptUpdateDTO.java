package com.yue.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Department update dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class DeptUpdateDTO {
    private Integer id;
    private String name;
}
