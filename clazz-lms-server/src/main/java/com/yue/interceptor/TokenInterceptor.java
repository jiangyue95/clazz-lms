package com.yue.interceptor;

import com.yue.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * Token 校验拦截器
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 获取到请求的路径
        String path = request.getRequestURI();

        // 2. 判断是否是登录请求，如果请求路径中包含/login，说明是登录操作，放行
        if (path.contains("/login")) {
            log.info("登录操作，放行");
            return true;
        }

        // 3. 获取请求头中的 token（JWT Token）
        String token = request.getHeader("token");

        // 4. 判断 token 是否为空，如果为空，说明没有登录，返回错误信息（响应 401 状态码）
        if (token == null || token.isEmpty()) {
            log.info("令牌为空，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return  false;
        }

        // 5. 如果 token 不为空，则调用 JWTUtils 类中的 parseJWTToken 方法，解析 token。校验失败，返回错误信息（响应 401 状态码）
        try {
            JWTUtils.parseJWTToken(token);
        } catch (Exception e) {
            log.info("令牌非法，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 6. 校验通过，则放行
        log.info("令牌合法，放行");
        return true;
    }
}
