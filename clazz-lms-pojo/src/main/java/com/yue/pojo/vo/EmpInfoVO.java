package com.yue.pojo.vo;

import com.yue.pojo.entity.EmpExpr;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpInfoVO {
    private Integer id;
    private String username;
    private String name;
    private Integer gender;
    private String image;
    private Integer job;
    private Integer salary;
    private LocalDate entryDate;
    private Integer status;
    private Integer deptId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<EmpExpr> exprList;
}
