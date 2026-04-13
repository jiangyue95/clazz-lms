package com.yue;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTTest {
    @Test
    public void testGenerateJwt() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("id", 1);
        dataMap.put("username", "admin");

        String secretString = "VGhpc2lzYWxvbmdsb25nc2VjcmV0dG9rZW5mb3JzZWN1cml0eQ==";
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));

        String jwt = Jwts.builder()
                .signWith(key)
                .claims(dataMap)
                .expiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .compact();

        System.out.println(jwt);
    }

    @Disabled("JWT is expired, ignore for now")
    @Test
    public void testParseJwt() {
        String token ="eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcm5hbWUiOiJhZG1pbiIsImV4cCI6MTc2NzEzMjQxOH0.u23Olqa_7HGg9hTSsMXw13txrTsXzfiqazOFeEUdxw8";
        String secretString = "VGhpc2lzYWxvbmdsb25nc2VjcmV0dG9rZW5mb3JzZWN1cml0eQ==";
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
        Claims claims =Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        System.out.println(claims);
    }
}
