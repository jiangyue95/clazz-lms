package com.yue.service.impl;

import com.yue.exception.RefreshTokenException;
import com.yue.pojo.entity.RefreshToken;
import com.yue.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final long refreshTokenExpireDays = 7L;


    @Override
    public String createRefreshToken(Long empId) {
        String token = UUID.randomUUID().toString().replace("-", "")
                + UUID.randomUUID().toString().replace("-", "");

        RefreshToken entity = new RefreshToken();
        entity.setEmpId(empId);
        entity.setToken(token);
        entity.setExpiryTime(LocalDateTime.now().plusDays(refreshTokenExpireDays));
        entity.setRevoked(false);

        refreshTokenRepository.save(entity);
        return token;
    }

    @Override
    public RefreshToken verifyAndGet(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token"));

        if (Boolean.TRUE.equals(refreshToken.getRevoked())) {
            throw new RefreshTokenException("Refresh token revoked");
        }

        if (refreshToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RefreshTokenException("Refresh token expired");
        }

        return refreshToken;
    }

    @Override
    public String rotate(RefreshToken oldToken) {
        oldToken.setRevoked(true);
        refreshTokenRepository.save(oldToken);
        return createRefreshToken(oldToken.getEmpId());
    }

    @Override
    public void revoke(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(item -> {
            item.setRevoked(true);
            refreshTokenRepository.save(item);
        });
    }
}
