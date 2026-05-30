package com.yue.pojo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Student update payload.
 *
 * <p>Does not carry an {@code id} field: the target student is identified by
 * the URL path ({@code PUT /students/{id}}), which is authoritative. Keeping
 * the id out of the body removes any ambiguity between a path id and a body id.
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class StudentUpdateDTO {
    @Size(min = 2, max = 10, message = "Student name must be {min} to {max} characters")
    private String name;

    @Size(max = 10, message = "Student number cannot exceed 10 characters")
    private String no;

    @Min(value = 1, message = "Gender must be 1 or 2")
    @Max(value = 2, message = "Gender must be 1 or 2")
    private Integer gender;

    @Size(max = 11, message = "Phone number must not exceed 11 numbers")
    private String phone;

    @Size(max = 18, message = "id number must not exceed 18 numbers")
    private String idCard;

    @Size(max = 100, message = "Student's address must not exceed {max} numbers")
    private String address;

    private Integer degree;

    @Min(value = 0, message = "Gender must be 0 or 1")
    @Max(value = 1, message = "Gender must be 0 or 1")
    private Integer isCollege;

    private LocalDate graduationDate;

    @Positive(message = "Class id must be a positive integer")
    private Integer clazzId;
}
