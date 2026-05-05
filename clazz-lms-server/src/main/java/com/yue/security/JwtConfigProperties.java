package com.yue.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * JWT configuration properties bound from application.yml under the "jwt" prefix.
 *
 * <p>This class externalizes JWT settings (secret key, token expiration) so that
 * sensitive values are no longer hardcoded in source. Production deployments
 * should override these via environment variables (e.g., JWT_SECRET).
 *
 * <p>Example application.yml:
 * <pre>
 * jwt:
 *   secret: ${JWT_SECRET:dev-only-placeholder}
 *   expiration-ms: 43200000
 * </pre>
 */
@Component
@ConfigurationProperties(prefix = "jwt")
@Validated
@Data
public class JwtConfigProperties {

    /** Base64-encoded HMAC-SHA secret. Must be at least 32 bytes (256 bits) when decoded. */
    @NotBlank
    private String secret;

    /** Token validity duration in milliseconds. */
    @Positive
    private long expirationMs;
}
