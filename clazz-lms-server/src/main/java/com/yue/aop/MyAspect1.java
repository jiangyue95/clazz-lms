// Custom AOP class
package com.yue.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Aspect // 标识当前是一个 AOP 类。
@Component // 标识为 Spring 组件，交给 Spring 容器管理。
@Slf4j
public class MyAspect1 {

    @Pointcut("execution(* com.yue.service.impl.ClazzServiceImpl.*(..))")
    private void pt(){}

    // Before advice: runs before the target method is executed.
    @Before("pt()")
    public void before() {
        log.info("Before ...");
    }

    // Around advice: runs before and after the target method is executed.
    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Around ... before ...");
        Object result = pjp.proceed();
        log.info("Around ... after ...");
        return result;
    }

    // After advice: runs after the target method is executed.
    @After("pt()")
    public void after() {
        log.info("After ...");
    }

    // After returning advice: runs after the target method is executed, but only if the method returns normally.
    @AfterReturning("pt()")
    public void afterReturning() {
        log.info("After returning ...");
    }

    // After throwing advice: runs after the target method is executed, but only if the method throws an exception.
    @AfterThrowing("pt()")
    public void afterThrowing() {
        log.info("After throwing ...");
    }
}
