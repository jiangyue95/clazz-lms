package com.yue.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Employee list DTO
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpSaveDTO {
    private Integer id;
    private String username;
    private String name;
    private String password;
    private Integer gender;
    private String image;
    private Integer job;
    private Integer salary;
    private LocalDate entryDate;
    private Integer deptId;
    private String deptName;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
