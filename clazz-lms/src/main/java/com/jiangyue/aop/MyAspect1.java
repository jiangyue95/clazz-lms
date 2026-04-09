package com.jiangyue.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Aspect // 标识当前是一个 AOP 类。
@Component // 标识为 Spring 组件，交给 Spring 容器管理。
@Slf4j
public class MyAspect1 {

    @Pointcut("execution(* com.jiangyue.service.impl.ClazzServiceImpl.*(..))")
    private void pt(){}

    // 前置通知，在目标方法运行之前
    @Before("pt()")
    public void before() {
        log.info("Before ...");
    }

    // 环绕通知，在目标运行之前、之后运行
    @Around("pt()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        log.info("Around ... before ...");
        Object result = pjp.proceed();
        log.info("Around ... after ...");
        return result;
    }

    // 后置通知，目标方法运行之后运行，无论是否出现异常都会执行
    @After("pt()")
    public void after() {
        log.info("After ...");
    }

    // 返回后通知，目标方法运行之后运行，如果出现异常不会运行
    @AfterReturning("pt()")
    public void afterReturning() {
        log.info("After returning ...");
    }

    // 返回后通知，目标方法运行之后运行，只有出现异常才会运行
    @AfterThrowing("pt()")
    public void afterThrowing() {
        log.info("After throwing ...");
    }
}
