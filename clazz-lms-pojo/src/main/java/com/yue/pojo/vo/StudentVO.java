package com.yue.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * student view object (VO)
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class StudentVO {
    private Integer id;

    // student name
    private String name;

    // student number
    private String no;

    // gender: 1, male; 2, female
    private Integer gender;

    // phone number
    private String phone;

    // ID number
    private String idCard;

    // Is the student from a college? 1, yes; 0, no
    private Integer isCollege;

    // address
    private String address;

    // highest degree
    // 1, middle school
    // 2, high school
    // 3, college
    // 4, bachelor
    // 5, master
    // 6, doctor
    private Integer degree;

    // graduate date
    private LocalDate graduationDate;

    // the id of clazz(class)
    private Integer clazzId;

    // the count of violation
    private Short violationCount;

    // the score of violation
    private Short violationScore;

    // create time
    private LocalDateTime createTime;

    // update time
    private LocalDateTime updateTime;

    // the name of clazz(class)
    // this name is searched by the clazzId
    private String clazzName;
}
