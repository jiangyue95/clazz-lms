package com.yue.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Employee login DTO(Data Transfer Object).
 *
 * <p>Bean Validation constraints are enforced when the controller annotates
 * the parameter with {@code @Valid}. Field-level violations propagate as
 * {@link org.springframework.web.bind.MethodArgumentNotValidException}, mapped
 * to HTTP 400 by {@code GlobalExceptionHandler}.
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpLoginDTO {

    @NotBlank(message = "Username is required")
    @Size(max = 20, message = "Username must not exceed {max} characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(max = 32, message = "Password length must not exceed {max} characters")
    private String password;
}
