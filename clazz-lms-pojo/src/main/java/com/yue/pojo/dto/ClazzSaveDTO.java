package com.yue.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Class (cohort) creation payload.
 *
 * <p>Carries the fields necessary to create a new class. All fields are
 * required at creation time; partial updates use {@link ClazzUpdateDTO}
 *
 * <p>Validation here covers <i>structural</i> constraints only:
 * presence, length, and numeric sign. Business rules such as
 * "endDate must be after beginDate" and "master Id must reference an
 * existing employee" are enforced in {@code ClazzService}, since they
 * require either cross-field reasoning or a database lookup.
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ClazzSaveDTO {

    @NotBlank(message = "Class name is required")
    @Size(min = 2, max = 30, message = "Class name must be {min} to {max} characters")
    private String name;

    @NotBlank(message = "Room is required")
    @Size(max = 20, message = "Room must not exceed {max} characters")
    private String room;

    @NotNull(message = "Begin date is required")
    private LocalDate beginDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Master (head teacher) is required")
    @Positive(message = "Master id must be a positive integer")
    private Integer masterId;

    @NotNull(message = "Subject is required")
    private Integer subject;
}
