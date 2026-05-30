package com.yue.pojo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Employee list / search query parameters.
 *
 * <p>Carries filters (name, gender, date range) and pagination
 * parameters for {@code GET /emps}. All fields are optional: callers
 * may omit any combination, and {@code page} / {@code pageSize} fall
 * back to sensible defaults if not provided.
 *
 * <p>Pagination bounds are enforced here as a defence against
 * unbounded queries: an unconstrained {@code pageSize} could be set
 * to a very large value and cause the database to materialise an
 * unreasonable result set. The cap of 100 covers all realistic UI
 * paging needs; bulk export should use a dedicated endpoint instead
 * of abusing pagination.
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpListQueryDTO {
    private String name;
    private Integer gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate begin;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    @Min(value = 1, message = "Page must be at least {value}")
    private Integer page=1;

    @Min(value = 1, message = "Page size must be at least {value}")
    @Max(value = 100, message = "Page size must not exceed {value}")
    private Integer pageSize=10;
}
