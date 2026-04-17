package com.yue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpJobOptionVO {
    private List jobList;
    private List dataList;
}
