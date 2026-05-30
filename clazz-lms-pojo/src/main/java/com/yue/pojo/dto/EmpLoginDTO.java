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

    @NotBlank(message = "Username must not be blank")
    @Size(max = 20, message = "Username must not exceed 50")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 1, max = 32, message = "Password length must be between 1 and 32")
    private String password;
}
