package com.yue.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    /** RFC 6750 mandates the "Bearer " prefix (note the trailing space). */
    private static final String BEARER_PREFIX = "Bearer ";

    /** Generic 401 error code - missing token, bad signature, malformed JWT. */
    private static final String ERROR_CODE_UNAUTHORIZED = "UNAUTHORIZED";

    /** Specific 401 error code - access token expired. Client should call /refresh. */
    private static final String ERROR_CODE_ACCESS_EXPIRED = "ACCESS_TOKEN_EXPIRED";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);

        if (token == null) {
            request.setAttribute("auth_error", ERROR_CODE_UNAUTHORIZED);
            log.info("Missing or malformed Authorization header at {}", request.getRequestURI());
        } else {
            try {
                Claims claims = jwtService.parseToken(token);
                // Check the type of token
                String tokenType = (String) claims.get("token_type");
                if (!JwtService.TOKEN_TYPE_ACCESS.equals(tokenType)) {
                    request.setAttribute("auth_error", ERROR_CODE_UNAUTHORIZED);
                    log.info("Non-access token used at {}: token_type={}",request.getRequestURI(), tokenType);
                } else {
                    Integer empId = (Integer) claims.get("id");
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(empId, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    log.info("Set authentication successfully: {}", empId);
                }
            } catch (ExpiredJwtException e) {
                request.setAttribute("auth_error", ERROR_CODE_ACCESS_EXPIRED);
                log.info("Access token expired at {}: {}", request.getRequestURI(), e.getMessage());
            } catch (JwtException e) {
                request.setAttribute("auth_error", ERROR_CODE_UNAUTHORIZED);
                log.info("Invalid token at {}: {}", request.getRequestURI(), e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Extracts a JWT from the {@code Authorization: Bearer <token>} header.
     *
     * @param request the incoming HTTP request
     * @return the raw JWT string, or {@code null} if the header is missing
     *         or doesn't begin with {@code "Bearer "}
     */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            return null;
        }
        return header.substring(BEARER_PREFIX.length());
    }
}


