package com.yue.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Department save dto
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class DeptSaveDTO {
    private String name;
}
