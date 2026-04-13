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
     * 生成 JWT 令牌
     * @param claims JWT 令牌中的数据
     * @return 生成的J WT 令牌
     */
    public static String generateJWTToken(Map<String, Object> claims) {
        return Jwts.builder()
                .signWith(SECRET_KEY)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .compact();
    }

    /**
     * 解析 JWT 令牌
     * @param jwtToken JWT 令牌
     * @return 解析结果：一个 Claims 对象
     */
    public static Claims parseJWTToken(String jwtToken) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }
}
