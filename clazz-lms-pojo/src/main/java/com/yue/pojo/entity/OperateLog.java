package com.yue.pojo.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * operate log entity
 */
@Data
public class OperateLog {

    // primary key
    private Integer id;

    // operate employee id
    private Integer operateEmpId; //操作人 ID

    // TODO: operate employee name from employee table
    // search by operateEmpId
    private String operateEmpName;

    // operate time
    private LocalDateTime operateTime;

    // the name of the class that the method belongs to
    private String className;

    // the name of the method that the employee called
    private String methodName;

    // the parameters of the method that the employee called
    private String methodParams;

    // the return value of the method that the employee called
    private String returnValue;

    // the cost time of the method that the employee called
    // in milliseconds
    private Long costTime;
}
