package com.yue.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JWTUtils {

    private static final String SECRET_STRING = "VGhpc2lzYWxvbmdsb25nc2VjcmV0dG9rZW5mb3JzZWN1cml0eQ==";
    private static final long EXPIRATION_TIME = 12 * 60 * 60 * 1000;
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_STRING));

    /**
     * Generate JWT token
     * @param claims JWT token data
     * @return generated JWT token String
     */
    public static String generateJWTToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SECRET_KEY)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    /**
     * Parse JWT token
     * @param jwtToken JWT token
     * @return parsed Claims object
     */
    public static Claims parseJWTToken(String jwtToken) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }
}
