package com.yue.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * student update data transfer object (DTO)
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class StudentUpdateDTO {
    private Integer id;
    private String name;
    private String no;
    private Integer gender;
    private String phone;
    private String idCard;
    private String address;
    private Integer degree;
    private Integer isCollege;
    private LocalDate graduationDate;
    private Integer clazzId;
}
