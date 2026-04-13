package com.yue.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//@Aspect // 标识当前是一个 AOP 类
@Component
@Slf4j
public class RecordTimeAspect {

    @Around("execution(* com.jiangyue.service.impl.DeptServiceImpl.*(..))")
    public Object recordTime(ProceedingJoinPoint pjp) throws Throwable {
        // 1. 记录方法运行的开始时间
        long begin = System.currentTimeMillis();
        // 2. 执行的原始方法
        Object result = pjp.proceed();
        // 3. 记录方法运行的结束时间，记录耗时
        long end = System.currentTimeMillis();
        log.info("方法 {} 执行耗时: {} ms", pjp.getSignature(), end - begin);
        return result;
    }
}
