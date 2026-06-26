package com.yue.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yue.pojo.dto.ErrorResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    private static final String ERROR_CODE_UNAUTHORIZED = "UNAUTHORIZED";
    private static final String ERROR_CODE_ACCESS_EXPIRED = "ACCESS_TOKEN_EXPIRED";

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        String errorCode;
        String message;

        Object authError = request.getAttribute("auth_error");
        if (ERROR_CODE_ACCESS_EXPIRED.equals(authError)) {
            errorCode = ERROR_CODE_ACCESS_EXPIRED;
            message = "Access token has expired";
        } else {
            errorCode = ERROR_CODE_UNAUTHORIZED;
            message = "Authentication required";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        ErrorResponseDTO body = new ErrorResponseDTO(
                errorCode,
                message,
                LocalDateTime.now(),
                request.getRequestURI()
        );
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}
