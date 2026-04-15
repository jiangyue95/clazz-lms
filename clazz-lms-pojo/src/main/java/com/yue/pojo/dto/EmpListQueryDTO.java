package com.yue.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Employee list search
 * Save the query parameters
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpListQueryDTO {
    private String name;
    private Integer gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    private Integer page=1;
    private Integer pageSize=10;
}
