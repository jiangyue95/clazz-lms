package com.yue.service;

import com.yue.pojo.entity.RefreshToken;

public interface RefreshTokenService {
    String createRefreshToken(Long empId);
    RefreshToken verifyAndGet(String token);
    String rotate(RefreshToken oldToken);
    void revoke(String token);
}
