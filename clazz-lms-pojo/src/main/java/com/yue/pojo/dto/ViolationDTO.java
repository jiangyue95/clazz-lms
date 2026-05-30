package com.yue.pojo.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Violation-recording payload.
 *
 * <p>Used by {@code POST /students/{id}/violations}. The student is identified
 * by the URL path; this body carries only the violation score to record.
 *
 * <p>The score is additive: each request adds {@code score} points to the
 * student's running total and increments their violation count by one.
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ViolationDTO {

    /**
     * Violation points to add to the student's running total. Must be positive.
     */
    @Positive(message = "Violation score must be positive")
    private Integer score;
}
