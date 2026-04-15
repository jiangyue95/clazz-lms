package com.yue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpVO {
    private Integer id;
    private String username;
    private String name;

//    sensitive field should not be added to VO
//    private String password;

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
