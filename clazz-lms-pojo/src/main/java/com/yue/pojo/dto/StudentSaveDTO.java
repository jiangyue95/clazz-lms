package com.yue.pojo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * student save data transfer object (DTO)
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class StudentSaveDTO {

    @NotBlank(message = "Student name cannot be blank")
    @Size(min = 2, max = 10, message = "Student name must be {min} to {max} characters")
    private String name;

    @NotNull(message = "Student number cannot be null")
    @Size(max = 10, message = "Student number cannot exceed 10 characters")
    private String no;

    @NotNull(message = "Gender cannot be null")
    @Min(value = 1, message = "Gender must be 1 or 2")
    @Max(value = 2, message = "Gender must be 1 or 2")
    private Integer gender;

    @NotBlank(message = "Phone number is required")
    @Size(max = 11, message = "Phone number must not exceed 11 numbers")
    private String phone;

    @NotBlank(message = "id number is required")
    @Size(max = 18, message = "id number must not exceed 18 numbers")
    private String idCard;

    @Size(max = 100, message = "Student's address must not exceed {max} numbers")
    private String address;


    private Integer degree;

    @NotNull(message = "Gender cannot be null")
    @Min(value = 0, message = "Gender must be 0 or 1")
    @Max(value = 1, message = "Gender must be 0 or 1")
    private Integer isCollege;

    private LocalDate graduationDate;

    @NotNull(message = "Class id is required")
    @Positive(message = "Class id must be a positive integer")
    private Integer clazzId;
}
