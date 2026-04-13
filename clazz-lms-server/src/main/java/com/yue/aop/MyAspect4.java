package com.yue.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//@Aspect
@Order(10)
@Component
@Slf4j
public class MyAspect4 {

    // 前置通知
    @Before("execution(* com.jiangyue.service.impl.ClazzServiceImpl.*(..))")
    public void before() {
        log.info("MyAspect4 -> before ...");
    }

    // 后置通知
    @After("execution(* com.jiangyue.service.impl.ClazzServiceImpl.*(..))")
    public void after() {
        log.info("MyAspect4 -> after ...");
    }
}
