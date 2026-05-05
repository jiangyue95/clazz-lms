package com.yue.pojo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Employee entity
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Emp {

    //ID, primary key
    private Long id;

    // username
    private String username;

    // password
    private String password;

    // name
    private String name;

    //gender: 1, male; 2, female
    private Integer gender;

    // phone number
    private String phone;

    //position:
    // 1, head teacher;
    // 2, lecturer;
    // 3, Student Affairs Supervisor;
    // 4, Teaching and Research Supervisor;
    // 5, Consultant
    private Integer job;

    private Integer salary;

    // avatar
    private String image;

    // Start date
    private LocalDate entryDate;

    // department id
    private Integer deptId;

    // create time
    private LocalDateTime createTime;

    // update time
    private LocalDateTime updateTime;
}
