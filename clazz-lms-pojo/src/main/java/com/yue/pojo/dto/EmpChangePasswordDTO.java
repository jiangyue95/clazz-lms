package com.yue.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Employee password change DTO.
 *
 * <p>Requires the current password for verification before accepting the new
 * one - defence against session hijacking, where a stolen token could
 * otherwise be used to lock the legitimate user out by changing their
 * password. Even with a valid auth token, knowing the current password
 * is a second factor.
 *
 * <p>Note: this DTO is used for an authenticated endpoint; the user's id
 * is taken from the JWT token's claims (via BaseContext), not from the
 * request body. Allowing the body to specify the user id would be a
 * privilege-escalation vector - anyone could change anyone else's password.
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EmpChangePasswordDTO {

    @NotBlank(message = "Current password is required")
    @Size(max = 100, message = "Current password must not exceed {max} characters")
    private String currentPassword;

    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 100, message = "New password must be {min} to {max} characters")
    private String newPassword;
}
