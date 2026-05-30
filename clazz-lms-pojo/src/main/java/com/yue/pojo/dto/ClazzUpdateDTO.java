package com.yue.pojo.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Class (cohort) update payload.
 *
 * <p>Used for partial updates via {@code PUT /clazzs/{id}}. The target
 * class is identified by the URL; this DTO carries only the fields
 * the client wishes to change. Omitted (null) fields are preserved as-is.
 *
 * <p>Validation semantics differ from {@link ClazzSaveDTO}: no field is
 * required (clients may update one field at a time), but any field that
 * <i>is</i> present must satisfy its structural constraint. This is the
 * standard Bean Validation pattern for partial updates — most constraint
 * annotations (e.g. {@code @Size}, {@code @Positive}) implicitly allow
 * null, so a field that's optional bur format-checked maps naturally to
 * "constraint without {@code @NotNull}".
 *
 * <p>Cross-field and referential-integrity rules (e.g. "endDate after
 * beginDate", "masterId references an existing employee") are enforced
 * in {@code ClazzService}.
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ClazzUpdateDTO {

    @Size(min = 2, max = 30, message = "Class name must be {min} to {max} characters")
    private String name;

    @Size(max = 20, message = "Room must not exceed {max} characters")
    private String room;

    private LocalDate beginDate;

    private LocalDate endDate;

    @Positive(message = "Master id must be a positive integer")
    private Integer masterId;

    private Integer subject;
}
