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
 * <p>Replaces the static {@code JWTUtils} utility class. As a Spring-managed bean,
 * it receives configuration via {@link JwtConfigProperties} (instead of hardcoded
 * constants), making the secret externalizable per environment.
 *
 * <p>The signing key is derived once at startup via {@link #initKey()} to avoid
 * repeating the conversion on every request.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

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
     * Generate a signed JWT token with the given claims.
     *
     * @param claims arbitrary key-value pairs to embed in the token payload
     * @return compact JWT string ({@code header.payload.signature})
     */
    public String generateToken(Map<String, Object> claims) {
        long nowMs = System.currentTimeMillis();
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(nowMs))
                .expiration(new Date(nowMs + config.getExpirationMs()))
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
