package com.yue.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request body for POST /refresh.
 *
 * <p>Carries the raw refresh token (the JWT string client received at login),
 * which the server hashes to look up the persisted refresh record in Redis.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshDTO {

    @NotBlank(message = "refreshToken is required")
    private String refreshToken;
}
