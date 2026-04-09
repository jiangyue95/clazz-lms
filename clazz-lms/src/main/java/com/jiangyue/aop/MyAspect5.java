package com.jiangyue.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class MyAspect5 {

    // 前置通知
//    @Before("execution(public void com.jiangyue.service.impl.ClazzServiceImpl.deleteClazzById(java.lang.Integer))")
    @Before("execution(void com.jiangyue.service.impl.ClazzServiceImpl.deleteClazzById(java.lang.Integer))")
    public void before() {
        log.info("MyAspect4 -> before ...");
    }

    // 后置通知
    @After("execution(* com.jiangyue.service.impl.ClazzServiceImpl.*(..))")
    public void after() {
        log.info("MyAspect5 -> after ...");
    }
}
