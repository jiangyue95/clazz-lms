package com.yue.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yue.pojo.dto.ErrorResponseDTO;
import com.yue.security.JwtService;
import com.yue.utils.BaseContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


/**
 * Token validation interceptor.
 *
 * <p>Reads the JWT from the standard {@code Authorization: Bearer <token>}
 * header (RFC 6750) rather than a custom {@code token} header. This aligns
 * the project with OAuth 2.0 conventions and lets HTTP tooling (curl,
 * Postman, browser dev tools, CORS) handle the credential header with
 * their built-in support.
 *
 * <p>On authentication failure, writes a structured {@link ErrorResponseDTO}
 * body matching the format used by {@code GlobalExceptionHandler}. The
 * {@code errorCode} field distinguishes expired access tokens
 * ({@code ACCESS_TOKEN_EXPIRED}) from other failure modes
 * ({@code UNAUTHORIZED}), so clients can decide whether to call /refresh
 * or redirect to login.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TokenInterceptor implements HandlerInterceptor {

    /** RFC 6750 mandates the "Bearer " prefix (note the trailing space). */
    private static final String BEARER_PREFIX = "Bearer ";

    /** Generic 401 error code - missing token, bad signature, malformed JWT. */
    private static final String ERROR_CODE_UNAUTHORIZED = "UNAUTHORIZED";

    /** Specific 401 error code - access token expired. Client should call /refresh. */
    private static final String ERROR_CODE_ACCESS_EXPIRED = "ACCESS_TOKEN_EXPIRED";

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // 1. Extract the bearer token from the Authorization header
        String token = extractBearerToken(request);

        // 2. Missing or malformed -> 401 UNAUTHORIZED
        if (token == null) {
            log.info("Missing or malformed Authorization header at {}", request.getRequestURI());
            writeUnauthorizedResponse(request, response, ERROR_CODE_UNAUTHORIZED, "Authentication required");
            return false;
        }

        // 3. Verify the token and stash the user id in BaseContext
        try {
            Claims claims = jwtService.parseToken(token);

            // Check the type of token
            String tokenType = (String) claims.get("token_type");
            if (!JwtService.TOKEN_TYPE_ACCESS.equals(tokenType)) {
                log.info("Non-access token used at {}: token_type={}",request.getRequestURI(), tokenType);
                writeUnauthorizedResponse(request, response, ERROR_CODE_UNAUTHORIZED, "Authentication required");
                return false;
            }

            BaseContext.setCurrentId((Integer) claims.get("id"));
            log.info("Authenticated empId: {}", BaseContext.getCurrentId());
            return true;
        } catch (ExpiredJwtException e) {
            // Access token expired - client should call /refresh, not re-login.
            log.info("Access token expired at {}: {}", request.getRequestURI(), e.getMessage());
            writeUnauthorizedResponse(request, response, ERROR_CODE_ACCESS_EXPIRED, "Access token has expired");
            return false;
        } catch (JwtException e) {
            // Any other JWT problem (bad signature, malformed, wrong type)
            // collapses to a single error code so attacker can't probe
            // which specific check failed.
            log.info("Invalid token at {}: {}", request.getRequestURI(), e.getMessage());
            writeUnauthorizedResponse(request, response, ERROR_CODE_UNAUTHORIZED, "Authentication required");
            return false;
        }
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

    /**
     * Write a 401 response with the same {@link ErrorResponseDTO} shape used
     * by {@code GlobalExceptionHandler}. Necessary because interceptors run
     * before {@code @RestControllerAdvice}, so we must hand-roll the JSON
     * serialization here.
     *
     * @param request current HTTP request (used to populate the path field)
     * @param response servlet response to write to
     * @param errorCode the specific error code - either ACCESS_TOKEN_EXPIRED
     *                  (so clients know to call /refresh) or UNAUTHORIZED
     *                  (collapses all other failure modes)
     * @param message human-readable message accompanying the errorCode
     * @throws IOException if writing the response fails
     */
    private void writeUnauthorizedResponse(HttpServletRequest request,
                                           HttpServletResponse response,
                                           String errorCode,
                                           String message) throws IOException {
        ErrorResponseDTO body = new ErrorResponseDTO(
                errorCode,
                message,
                LocalDateTime.now(),
                request.getRequestURI()
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }

    /**
     * Clears the current user id from BaseContext.
     *
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler the handler (or {@link HandlerMethod}) that started asynchronous
     * execution, for type and/or instance examination
     * @param ex any exception thrown on handler execution, if any; this does not
     * include exceptions that have been handled through an exception resolver
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        BaseContext.remove();
    }
}
