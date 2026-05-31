package com.yue.pojo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpUpdateDTO {

    @Size(min = 2, max = 20, message = "Employee username must be {min} to {max} characters")
    private String username;

    @Size(min = 2, max = 10, message = "Employee name must be {min} to {max} characters")
    private String name;

    @Min(value = 1, message = "Gender must be 1 or 2")
    @Max(value = 2, message = "Gender must be 1 or 2")
    private Integer gender;

    @Size(max = 255, message = "The length of photo address must not exceed {max} characters")
    private String image;

    @Positive(message = "Department id must be a positive integer")
    private Integer deptId;

    @Pattern(regexp = "\\d{11}", message = "Phone number must be 11 digits")
    private String phone;

    private LocalDate entryDate;

    private Integer job;

    private Integer salary;

    @Valid
    private List<EmpExprDTO> exprList;
}
