package com.yue.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Employee login DTO(Data Transfer Object)
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpLoginDTO {

    // Employee username
    private String username;

    // Employee password
    private String password;
}
