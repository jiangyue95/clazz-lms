package com.yue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpExprVO {
    private Integer id;
    private LocalDate begin;
    private LocalDate end;
    private String company;
    private String job;
    private Integer empId;
}
