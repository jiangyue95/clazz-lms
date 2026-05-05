package com.yue.service.impl;

import com.yue.pojo.entity.Emp;
import com.yue.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final String secret = "a secret string";
    private final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    private final long accessTokenExpireMs = 15  * 60 * 1000L;

    @Override
    public String generateAccessToken(Emp emp) {
        return Jwts.builder()
                .signWith(key)
                .claim("username", emp.getUsername())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpireMs))
                .compact();
    }

    @Override
    public Long parseEmpId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.valueOf(claims.getSubject());
    }

    @Override
    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
