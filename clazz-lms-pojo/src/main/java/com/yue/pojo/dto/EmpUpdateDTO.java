package com.yue.pojo.dto;

import com.yue.pojo.entity.EmpExpr;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpUpdateDTO {
    private Long id;
    private String username;
    private String name;
    private Integer gender;
    private String image;
    private Integer deptId;
    private String phone;
    private LocalDate entryDate;
    private Integer job;
    private Integer salary;
    private List<EmpExpr> exprList;
}
