package com.yue.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Single work-experience entry, used as a nested element of
 * {@link EmpSaveDTO} and {@link EmpUpdateDTO}.
 *
 * <p>Carries only the fields a client may set. The {@code id} (auto-generated
 * by the database) and {@code empId} (set server-side from the owning
 * employee) are intentionally absent: allowing the client to specify them
 * would let one client modify another's experience records, or claim an
 * existing record by id.
 *
 * <p>All fields are required. Work experience is historical (events that
 * have already happened), so both {@code begin} and {@code end} must be
 * provided.
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpExprDTO {

    @NotNull(message = "Begin date is required")
    private LocalDate begin;

    @NotNull(message = "End date is required")
    private LocalDate end;

    @NotBlank(message = "Company is required")
    @Size(max = 50, message = "Company must not exceed {max} characters")
    private String company;

    @NotBlank(message = "Job is required")
    @Size(max = 50, message = "Job must not exceed {max} characters")
    private String job;
}
