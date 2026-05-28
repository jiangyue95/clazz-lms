package com.yue.pojo.dto;

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
 * === The id field will be removed in commit 4. ===
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
