package com.yue.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * employee experience entity
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class EmpExpr {

    // primary key
    private Integer id;

    // employee id
    private Integer empId;

    // begin date
    private LocalDate begin;

    // end date
    private LocalDate end;

    // company name
    private String company;

    // position in the company
    private String job;
}
