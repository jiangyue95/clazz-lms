package com.yue.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * JWT generation and parsing service.
 *
 * <p>As a Spring-managed bean,it receives configuration via {@link JwtConfigProperties}
 * (instead of hardcoded constants), making the secret externalizable per environment.
 *
 * <p>The signing key is derived once at startup via {@link #initKey()} to avoid
 * repeating the conversion on every request.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    /** Token type claim - distinguishes access from refresh tokens. */
    private static final String TOKEN_TYPE_CLAIM = "token_type";

    /** Token type value for short-lived access tokens. */
    public static final String TOKEN_TYPE_ACCESS = "access";

    /** Token type value for long-lived refresh tokens. */
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    private final JwtConfigProperties config;

    /**
     * Cached signing key, derived from {@link JwtConfigProperties#getSecret()}.
     * Initialized once after dependency injection completes.
     */
    private SecretKey signingKey;

    @PostConstruct
    void initKey() {
        byte[] keyBytes = config.getSecret().getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        log.info("JwtService initialized with key length: {} bytes", keyBytes.length);
    }

    /**
     * Generate a short-lived access token for authenticating business requests.
     *
     * @param claims subject identity claims (typically emp id, username)
     * @return compact JWT string
     */
    public String generateAccessToken(Map<String, Object> claims) {
        claims.put(TOKEN_TYPE_CLAIM, TOKEN_TYPE_ACCESS);
        return buildToken(claims, config.getAccessExpirationMs());
    }

    /**
     * Generate a long-lived refresh token, used only at the /auth/refresh
     * endpoint to obtain a new access token.
     *
     * @param claims subject identity claims (typically emp id, username)
     * @return compact JWT string
     */
    public String generateRefreshToken(Map<String, Object> claims) {
        claims.put(TOKEN_TYPE_CLAIM, TOKEN_TYPE_REFRESH);
        return buildToken(claims, config.getRefreshExpirationMs());
    }

    /**
     * Shared token building logic - claims + signed JWT with given TTL.
     * Private because callers should use {@link #generateRefreshToken} or
     * {@link #generateRefreshToken} so the access/refresh distinction is
     * explicit at the call site.
     */
    private String buildToken(Map<String, Object> claims, long ttlMs) {
        long nowMs = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(nowMs))
                .expiration(new Date(nowMs + ttlMs))
                .signWith(signingKey)
                .compact();
    }

    /**
     * Verify and parse a JWT, returning its claims.
     *
     * @param token compact JWT string
     * @return decoded claims if signature and expiration area valid
     * @throws io.jsonwebtoken.JwtException if the token is invalid or expired
     */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
