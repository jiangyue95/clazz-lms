package com.yue.config;

import com.yue.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC configuration
 *
 * <p>Register the {@link TokenInterceptor} that performs JWT validation
 * for protected endpoints, and whitelists public endpoints (login, registration,
 * token refresh) as well as OpenAPI documentation routes (Swagger UI and
 * its static resources).
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**") // Intercept all requests
                .excludePathPatterns(
                        // --- Authentication endpoints (no token yet) ---
                        "/login",
                        "/register",
                        "/refresh",

                        // --- Browser noise ---
                        "/favicon.ico",

                        // --- OpenAPI documentation ---
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-ui/**",
                        "/webjars/**"
                ); // The login interface is not intercepted
    }
}
