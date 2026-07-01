package com.yue.security;

import com.yue.pojo.enums.Job;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockEmpSecurityContextFactory.class)
public @interface WithMockEmp {
    int empId() default 1;
    Job role() default Job.HEAD_TEACHER;
}
