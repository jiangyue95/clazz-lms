package com.yue.config;

import com.yue.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Custom Web Config class
 * Not in use, this is an example
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**") // Intercept all requests
                .excludePathPatterns("/login", "/favicon.ico", "/doc.html", "/v3/api-docs/**", "/swagger-ui/**", "/webjars/**"); // The login interface is not intercepted
    }
}
