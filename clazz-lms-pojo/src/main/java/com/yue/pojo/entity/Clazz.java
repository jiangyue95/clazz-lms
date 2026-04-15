package com.yue.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * clazz(class) entity
 * Because class is a reserved word in Java,
 * hence use clazz.
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Clazz {

    // primary key
    private Integer id;

    // class name
    private String name;

    // classroom
    private String room;

    // class begin date
    private LocalDate beginDate;

    // class end date
    private LocalDate endDate;

    // the id of head teacher of the class
    private Integer masterId;

    // subject：
    // 1, Java;
    // 2, Frontend;
    // 3, big data;
    // 4, python;
    // 5, Go
    // 6, Embedded development
    private Integer subject;

    // create time
    private LocalDateTime createTime;

    // update time
    private LocalDateTime updateTime;

    // the name of head teacher of the class
    private String masterName;

    // status:
    // 1, active;
    // 2, inactive;
    // TODO not a entity of database entity
    // this status is calculated by the beginDate and endDate
    private String status;
}
