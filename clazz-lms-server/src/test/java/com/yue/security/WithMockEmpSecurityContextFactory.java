package com.yue.security;

import com.yue.pojo.enums.Job;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

public class WithMockEmpSecurityContextFactory implements WithSecurityContextFactory<WithMockEmp> {
    @Override
    public SecurityContext createSecurityContext(WithMockEmp annotation) {

        Job role = annotation.role();
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                annotation.empId(),
                null,
                authorities
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
