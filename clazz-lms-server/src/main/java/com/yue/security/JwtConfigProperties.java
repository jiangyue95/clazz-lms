package com.yue.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtConfigProperties(
        String secretKey,
        long accessTokenExpireSeconds
) {
}
