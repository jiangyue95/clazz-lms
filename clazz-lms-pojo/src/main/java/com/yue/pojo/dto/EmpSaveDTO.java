package com.yue.pojo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Employee list DTO
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpSaveDTO {

    @NotBlank(message = "Employee username is required")
    @Size(min = 2, max = 20, message = "Employee username must be {min} to {max} characters")
    private String username;

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 10, message = "Employee name must be {min} to {max} characters")
    private String name;

    @NotNull(message = "Gender cannot be null")
    @Min(value = 1, message = "Gender must be 1 or 2")
    @Max(value = 2, message = "Gender must be 1 or 2")
    private Integer gender;

    @Size(max = 255, message = "The length of photo address must not exceed {max} characters")
    private String image;

    @NotNull(message = "Department is required")
    @Positive(message = "Department id must be a positive integer")
    private Integer deptId;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{11}", message = "Phone number must be 11 digits")
    private String phone;

    @NotNull(message = "Entry date is required")
    private LocalDate entryDate;

    @NotNull(message = "Job is required")
    private Integer job;

    @NotNull(message = "Salary is required")
    private Integer salary;

    @Valid
    private List<EmpExprDTO> exprList;
}
