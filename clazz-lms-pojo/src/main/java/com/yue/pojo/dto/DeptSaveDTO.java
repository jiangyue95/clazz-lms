package com.yue.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for creating a new department.
 * <p>
 * Validation constraints:
 * <ul>
 *     <li>{@code name} must not be blank - rejects null, empty, and whitespace-only strings</li>
 *     <li>{@code name} length must be between 2 and 50 characters.</li>
 * </ul>
 * Validation is triggered when the controller method parameter is annotated with {@code @Valid}.
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class DeptSaveDTO {

    @NotBlank(message = "Department name must not be blank")
    @Size(min = 2, max = 50, message = "Department name must be between 2 and 50 characters")
    private String name;
}
