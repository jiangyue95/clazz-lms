// An AOP class for logging method calls
package com.yue.aop;

import com.yue.mapper.OperateLogMapper;
import com.yue.pojo.entity.OperateLog;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class LogAspect {

    private final OperateLogMapper operateLogMapper;

    @Around("@annotation(com.yue.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {

        // 1. Record the start time of the method call
        long begin = System.currentTimeMillis();

        // 2. Execute the target method
        Object result = joinPoint.proceed();

        // 3. Record the end time of the method call
        long end = System.currentTimeMillis();

        // Calculate the cost time of the method call
        long costTime = end - begin;

        // Assemble the operate log entity
        OperateLog operateLog = new OperateLog();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Integer currentId = (auth != null && auth.getPrincipal() instanceof Integer)
                ? (Integer) auth.getPrincipal()
                : null;

        operateLog.setOperateEmpId(currentId);
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());
        operateLog.setMethodName(joinPoint.getSignature().getName());
        operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));
        operateLog.setReturnValue(result != null ? result.toString() : "null");
        operateLog.setCostTime(costTime);

        operateLogMapper.insert(operateLog);

        log.info("AOP Record operate log: {}", operateLog);

        return result;
    }
}
