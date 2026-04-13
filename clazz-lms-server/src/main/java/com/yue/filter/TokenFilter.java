package com.yue.filter;

import com.yue.utils.BaseContext;
import com.yue.utils.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest  request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取到请求的路径
        String path = request.getRequestURI();

        // 2. 判断是否是登录请求，如果请求路径中包含/login，说明是登录操作，放行
        if (path.contains("/login")) {
            log.info("登录操作，放行");
            filterChain.doFilter(request, response);
            return;
        }

        // 3. 获取请求头中的 token（JWT Token）
        String token = request.getHeader("token");

        // 4. 判断 token 是否为空，如果为空，说明没有登录，返回错误信息（响应 401 状态码）
        if (token == null || token.isEmpty()) {
            log.info("令牌为空，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 5. 如果 token 不为空，则调用 JWTUtils 类中的 parseJWTToken 方法，解析 token。校验失败，返回错误信息（响应 401 状态码）
        try {
            Claims claims = JWTUtils.parseJWTToken(token);
            // 获取当前登录用户的 empId
            Integer empIdInt = (Integer) claims.get("id");
            BaseContext.setCurrentId(empIdInt);
            log.info("当前登录用户 empId: {}", empIdInt);
        } catch (Exception e) {
            log.info("令牌非法，响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 6. 校验通过，则放行
        log.info("令牌合法，放行");
        filterChain.doFilter(request, response);

        // 7. 删除 BaseContext 中保存的当前登录用户的 empId
        BaseContext.remove();
    }
}
