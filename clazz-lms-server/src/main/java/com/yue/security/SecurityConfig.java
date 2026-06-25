package com.yue.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Centralizes security-related beans.
 *
 * <p>Expose a {@link PasswordEncoder} bean used for hashing and verifying
 * employee passwords. Backed by {@link BCryptPasswordEncoder} with the
 * library's default work factor (10), which is the current Spring Security
 * recommendation: high enough to make brute-force expensive (~100ms per
 * hash on typical hardware), low enough that legitimate logins remain
 * imperceptible to users.
 *
 * <p>The bean is exposed via the {@code PasswordEncoder} interface (rather
 * than the concrete {@code BCryptPasswordEncoder}) to keep callers
 * decoupled from the specific algorithm. A future migration to Argon2 or
 * to {@code PasswordEncoderFactories.createDelegationPasswordEncoder()} -
 * which supports multiple algorithms with format prefixes (e.g.
 * {@code {bcrypt}...}, {@code {argon2}...}) - would be a single-line
 * change here, transparent to business code.
 *
 * <p>Note: this project intentionally depends only on
 * {@code spring-security-crypto} (the cryptographic utilities), not
 * {@code spring-boot-starter-security} (the full security stack). Adding
 * the starter would activate Spring Security's autoconfiguration -
 * formLogin, httpBasic, CSRF - which would conflict with the existing
 * {@code TokenInterceptor}-based auth flow. Migrating to Spring Security's
 * filter chain is a separate, larger refactor for future PR.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Close CSRF protection
                .csrf(csrf -> csrf.disable())
                // 2. Does not create HttpSession, totally stateless
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 3. Temporary allow all request, replace the default "ALL requests need to be authorised"
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll());
        return http.build();
    }
}
