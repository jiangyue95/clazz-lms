package com.yue.interceptor;

import com.yue.security.JwtService;
import com.yue.utils.BaseContext;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * Token validation interceptor.
 *
 * <p>Reads the JWT from the standard {@code Authorization: Bearer <token>}
 * header (RFC 6750) rather than a custom {@code token} header. This aligns
 * the project with OAuth 2.0 conventions and lets HTTP tooling (curl,
 * Postman, browser dev tools, CORS) handle the credential header with
 * their built-in support.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    /** RFC 6750 mandates the "Bearer " prefix (note the trailing space). */
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. Extract the beater token from the Authorization header
        String token = extractBearerToken(request);

        // 2. Missing or malformed -> 401
        if (token == null) {
            log.info("Missing or malformed Authorization header - 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 3. Verify the token and stash the user id in BaseContext
        try {
            Claims claims = jwtService.parseToken(token);
            BaseContext.setCurrentId((Integer) claims.get("id"));
            log.info("Authenticated empId: {}", BaseContext.getCurrentId());
        } catch (Exception e) {
            log.info("Token invalid - 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }

    /**
     * Extracts a JWT from the {@code Authorization: Bearer <token>} header.
     *
     * @param request the incoming HTTP request
     * @return the raw JWT string, or {@code null} if the header is missing
     *         or doesn't begin with {@code "Bearer "}
     */
    private String extractBearerToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return header.substring(BEARER_PREFIX.length());
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        BaseContext.remove();
    }
}
