package com.yue.security;

import com.yue.pojo.entity.Emp;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtTokenService {

    private final JwtConfigProperties properties;
    private final SecretKey secretKey;

    public JwtTokenService(JwtConfigProperties properties) {
        this.properties = properties;
        this.secretKey = Keys.hmacShaKeyFor(properties.secretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(Emp emp) {
        Instant now = Instant.now();
        Instant expiry = now.plus(properties.accessTokenExpireMinutes(), ChronoUnit.MINUTES);

        return Jwts.builder()
                .subject(String.valueOf(emp.getId()))
                .claim("username", emp.getUsername())
                .claim("name", emp.getName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
