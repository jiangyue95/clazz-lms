package com.yue.interceptor;

import com.yue.security.JwtConfigProperties;
import com.yue.security.JwtService;
import com.yue.utils.BaseContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * Token 校验拦截器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. get the JWT Token in the request header
        String token = request.getHeader("token");

        // 2. If the token is empty, it means the user has not logged in,
        // return an error message (response 401 status code)
        if (token == null || token.isEmpty()) {
            log.info("Token is empty, response 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return  false;
        }

        // 3. if the token is not empty, parse it via JwtService
        // if the token is invalid, return an error message (response 401 status code)
        try {
            Claims claims = jwtService.parseToken(token);
            BaseContext.setCurrentId((Integer) claims.get("id"));
            log.info("Current user empId: {}", BaseContext.getCurrentId());
        } catch (Exception e) {
            log.info("Token invalid，response 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 4. if token is valid, return true to allow the request to continue processing
        log.info("Token valid, continue processing");
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        BaseContext.remove();
    }
}
