package com.yue.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Employee self-registration DTO.
 *
 * <p>Accepts only fields the user should be able to set for themselves:
 * username, password, name, phone, gender. Admin-controlled fields
 * (dept_id, job, salary) are deliberately excluded - including them
 * would let any registering user assign themselves to a department
 * or set their own salary, which is a privilege-escalation vector.
 * Those fields default to NULL and must be set by admin via the
 * existing {@code PUT /emps/{id}} endpoint.
 *
 * <p>Note: in a typical school administration domain, employees would
 * be onboarded by HR rather than self-registering. This endpoint is
 * implemented as a learning exercise; production use would either
 * remove the endpoint or restrict it to admin callers only.
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpRegisterDTO {

    @NotBlank(message = "username must not be blank")
    @Size(min = 3, max = 20, message = "username length must be between 3 and 20")
    private String username;

    @NotBlank(message = "password must not be blank")
    @Size(min = 8, max = 100, message = "password length must be between 8 and 100")
    private String password;

    @NotBlank(message = "name must not be blank")
    @Size(max = 10, message = "name length must not exceed 10")
    private String name;

    @NotBlank(message = "phone must not be blank")
    @Pattern(regexp = "\\d{11}", message = "phone must be exactly 11 digits")
    private String phone;

    @NotNull(message = "gender must be provided")
    private Integer gender;

}
