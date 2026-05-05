package com.yue.service.impl;

import com.yue.exception.UnauthorizedException;
import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.entity.Emp;
import com.yue.pojo.entity.RefreshToken;
import com.yue.pojo.vo.LoginVO;
import com.yue.security.JwtConfigProperties;
import com.yue.service.AuthService;
import com.yue.service.EmpService;
import com.yue.service.JwtService;
import com.yue.service.RefreshTokenService;
import com.yue.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EmpService empService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final JwtConfigProperties jwtConfigProperties;

    @Override
    @Transactional
    public LoginVO login(EmpLoginDTO dto, HttpServletResponse response) {
        log.info("Login attempt for username: {}", dto.getUsername());

        Emp emp = empService.authenticate(dto);
        if (emp == null) {
            log.warn("Login failed: invalid credentials for username: {}", dto.getUsername());
            throw new UnauthorizedException("Wrong username or password");
        }

        log.info("Login successful for empId: {}, username: {}", emp.getId(), emp.getUsername());
        String accessToken = jwtService.generateAccessToken(emp);
        String refreshToken = refreshTokenService.createRefreshToken(emp.getId());

        CookieUtil.addRefreshTokenCookie(response, refreshToken);

        return LoginVO.builder()
                .id(emp.getId())
                .username(emp.getUsername())
                .name(emp.getName())
                .accessToken(accessToken)
                .accessTokenExpiresIn(jwtConfigProperties.accessTokenExpireSeconds())
                .build();
    }


    @Override
    @Transactional
    public LoginVO refresh(HttpServletRequest request, HttpServletResponse response) {
        log.debug("Refresh token request received");
        String refreshToken = CookieUtil.getCookieValue(request, "refresh_token");
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new UnauthorizedException("Refresh token missing");
        }

        RefreshToken tokenRecord = refreshTokenService.verifyAndGet(refreshToken);

        Emp emp = empService.getById(tokenRecord.getEmpId());
        String newAccessToken = jwtService.generateAccessToken(emp);

        String newRefreshToken = refreshTokenService.rotate(tokenRecord);
        CookieUtil.addRefreshTokenCookie(response, newRefreshToken);

        return LoginVO.builder()
                .id(emp.getId())
                .username(emp.getUsername())
                .name(emp.getName())
                .accessToken(newAccessToken)
                .accessTokenExpiresIn(jwtConfigProperties.accessTokenExpireSeconds())
                .build();
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookieValue(request, "refresh_token");
        if (refreshToken != null && !refreshToken.isBlank()) {
            refreshTokenService.revoke(refreshToken);
        }
        CookieUtil.clearRefreshTokenCookie(response);
        log.info("Logout completed");
    }
}
