package com.yue.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yue.pojo.entity.RefreshToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Stores and retrieves refresh tokens in Redis.
 *
 * <p>Key design: {@code refresh_token:{tokenHash}} -> JSON-serialized
 * {@link RefreshToken}. This supports the primary lookup ("given a token
 * presented at /auth/refresh, is it valid?"). It does NOT support
 * "revoke all tokens for an employee" - that would need a second index
 * ({@code refresh_user:{empId}} -> set of hashes) and is deferred as a
 * follow-up.
 *
 * <p>Two expiration mechanisms work together:
 * <ul>
 *     <li>Redis TTL - set on write, so naturally expired tokens vanish.</li>
 *     <li>{@code revokedAt} - soft-delete; {@link #isValid} checks it.</li>
 * </ul>
 *
 * <p>Uses {@link StringRedisTemplate} with explicit JSON (de)serialization
 * via Jackson, rather than {@code @RedisHash} repositories. The explicit
 * approach keeps the Redis operations visible and debuggable.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    private static final String KEY_PREFIX = "refresh_token:";

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Persist a refresh token, with a Redis TTL matching its natural expiry.
     *
     * @param token the refresh token to store
     */
    public void save(RefreshToken token) {
        String key = KEY_PREFIX + token.getTokenHash();
        try {
            String json = objectMapper.writeValueAsString(token);
            // TTL = time from now until expiresAt. Redis will auto-delete the
            // key when this elapses, so naturally expired tokens need no cleanup.
            Duration ttl = Duration.between(LocalDateTime.now(), token.getExpiresAt());
            redisTemplate.opsForValue().set(key, json, ttl);
            log.info("Stored refresh token for empId={} (ttl={}s)", token.getEmpId(), ttl.toSeconds());
        } catch (Exception e) {
            // Serialization failure or Redis connectivity issue - surface it
            // rather than silently failing to persist the token.
            throw new IllegalStateException("Failed to store refresh token", e);
        }
    }

    /**
     * Look up a refresh token by its SHA-256 hash.
     *
     * @param tokenHash the SHA-256 hex hash of the raw token
     * @return the stored token, or {@code null} if not found (expired/never existed)
     */
    public RefreshToken findByTokenHash(String tokenHash) {
        String key = KEY_PREFIX + tokenHash;
        String json = redisTemplate.opsForValue().get(key);
        if (json == null) {
            // Key absent: either the token never existed, or its TTL elapsed.
            return null;
        }
        try {
            return objectMapper.readValue(json, RefreshToken.class);
        } catch (Exception e) {
            // A malformed value in Redis is a data-integrity problem - log and
            // treat as "not found" rather than throwing into the caller.
            log.error("Failed to deserialize refresh token for key={}", key, e);
            return null;
        }
    }

    /**
     * Soft-delete a single refresh token by marking its {@code revokedAt}.
     *
     * <p>The key is NOT deleted from Redis - it remains until its TTL elapses,
     * but {@link #isValid} will reject it. This preserves the record for any
     * auditing within the token's original lifetime.
     *
     * @param tokenHash the SHA-256 hex hash of the token to revoke
     */
    public void revokeByTokenHash(String tokenHash) {
        RefreshToken token = findByTokenHash(tokenHash);
        if (token == null) {
            // Nothing to revoke - already gone or never existed. Not an error.
            log.info("Revoke requested for unknown/expired token hash");
            return;
        }
        if (token.getRevokedAt() != null) {
            // Already revoked - idempotent, nothing to do.
            return;
        }
        token.setRevokedAt(LocalDateTime.now());
        // Re-save preserves the remaining TTL via save()'s recomputation from
        // expiresAt - the revoked record won't outlive the original token.
        save(token);
        log.info("Revoked refresh token for empId={}", token.getEmpId());
    }

    /**
     * Check whether a stored token is currently usable.
     *
     * <p>A token is valid only if it exists, is not soft-deleted
     * ({@code revokedAt == null}), and has not passed {@code expiresAt}.
     * The {@code expiresAt} check is belt-and-suspenders alongside Redis TTL:
     * it guards the brief window where a key might linger, and it makes the
     * validity rule explicit in code rather than implicit in Redis config.
     *
     * @param token a token loaded via {@link #findByTokenHash}, or {@code null}
     * @return {@code true} if the token is non-null, not revoked, not expired
     */
    public boolean isValid(RefreshToken token) {
        if (token == null) {
            return false;
        }
        if (token.getRevokedAt() != null) {
            return false;
        }
        return token.getExpiresAt().isAfter(LocalDateTime.now());
    }
}
