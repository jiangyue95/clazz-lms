package com.yue.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtService jwtService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtService);
        http
                // 1. Close CSRF protection
                .csrf(csrf -> csrf.disable())
                // 2. Does not create HttpSession, totally stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 3. Authorization rules: whitelist public endpoints, everything else requires authentication
                .authorizeHttpRequests(auth -> auth
                        // Authentication endpoints (no token yet)
                        .requestMatchers("/login", "/register", "/refresh").permitAll()
                        // Browser noise
                        .requestMatchers("/favicon.ico").permitAll()
                        // OpenAPI documentation
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui.html",
                                "/swagger-ui/**", "/webjars/**").permitAll()
                        // Logs
                        .requestMatchers(HttpMethod.GET, "/logs/**").hasRole("ADMIN")
                        // Emp: employee
                        .requestMatchers(HttpMethod.GET, "/emps/**").hasAnyRole("ADMIN", "TEACHING_AND_RESEARCH_SUPERVISOR")
                        .requestMatchers(HttpMethod.POST, "/emps/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/emps/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/emps/**").hasRole("ADMIN")
                        // Dept: department
                        .requestMatchers(HttpMethod.GET, "/depts/**").hasAnyRole("ADMIN", "TEACHING_AND_RESEARCH_SUPERVISOR")
                        .requestMatchers(HttpMethod.POST, "/depts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/depts/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/depts/**").hasRole("ADMIN")
                        // Clazz: class
                        .requestMatchers(HttpMethod.POST, "/clazzs/**").hasAnyRole("ADMIN", "TEACHING_AND_RESEARCH_SUPERVISOR")
                        .requestMatchers(HttpMethod.PUT, "/clazzs/**").hasAnyRole("ADMIN", "TEACHING_AND_RESEARCH_SUPERVISOR")
                        .requestMatchers(HttpMethod.DELETE, "/clazzs/**").hasAnyRole("ADMIN", "TEACHING_AND_RESEARCH_SUPERVISOR")
                        // Student
                        .requestMatchers(HttpMethod.DELETE, "/students/**").hasAnyRole("ADMIN", "STUDENT_AFFAIRS_SUPERVISOR")
                        .requestMatchers(HttpMethod.POST, "/students/**").hasAnyRole("ADMIN", "STUDENT_AFFAIRS_SUPERVISOR", "HEAD_TEACHER")
                        .requestMatchers(HttpMethod.PUT, "/students/**").hasAnyRole("ADMIN", "STUDENT_AFFAIRS_SUPERVISOR", "HEAD_TEACHER")
                        // Any request
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
