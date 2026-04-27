package com.yue.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload for updating an existing department.
 * <p>
 * Note: the {@code id} is intentionally absent from this DTO. The identifier
 * is sourced exclusively from the URL path to prevent mass-assignment - a
 * body field name {@code id} would be silently ignored by the controller.
 * <p>
 * Validation constraints mirror {@link DeptSaveDTO}: name must be non-blank
 * and 2-50 characters. They're declared explicitly here rather than inherited
 * to keep the contract self-documenting and to allow divergence in the future.
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class DeptUpdateDTO {

    @NotBlank(message = "Department name must not be blank")
    @Size(min = 2, max = 50, message = "Department name must be between 2 and 50 characters")
    private String name;
}
