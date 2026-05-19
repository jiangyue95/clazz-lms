package com.yue.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * A refresh token record, stored in Redis.
 *
 * <p>Only the SHA-256 hash of the raw token is stored - never the raw token
 * itself. A Redis compromise therefore leaks hashes, not usable credentials.
 * The raw token is returned to the client exactly once (at login) and never
 * persisted server-side in recoverable form.
 *
 * <p>Two independent expiration mechanisms apply:
 * <ul>
 *     <li>Redis TTL - the key is set to expire automatically, so naturally
 *         expired tokens simply vanish from storage.</li>
 *     <li>{@code revokedAt} - a soft-delete marker for deliberate invalidation
 *         (e.g. single-token revocation). A revoked token may still physically
 *         exist in Redis until its TTL elapses, so validation must check this
 *         field, not merely the key's existence.</li>
 * </ul>
 *
 * <p>{@code expiresAt} duplicates information that Redis TTL also enforces.
 * It is stored anyway as explicit business data: it survives a potential
 * future migration away from Redis, and it makes the record self-describing
 * for auditing ("when was this token supposed to expire?")
 */
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RefreshToken {

    /** SHA-256 hex hash of the raw token. Also serves as the Redis key suffix. */
    private String tokenHash;

    /** The employee this token authenticates */
    private Integer empId;

    /** When this token was issued (at login) */
    private LocalDateTime issuedAt;

    /** When this token naturally expires (issuedAt + 7 days). */
    private LocalDateTime expiresAt;

    /** Soft-delete marker: null = active, non-null = deliberately revoked. */
    private LocalDateTime revokedAt;
}
