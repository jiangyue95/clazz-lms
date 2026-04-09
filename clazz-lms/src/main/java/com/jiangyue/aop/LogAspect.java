package com.jiangyue.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiangyue.mapper.OperateLogMapper;
import com.jiangyue.pojo.OperateLog;
import com.jiangyue.utils.BaseContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Component
@Aspect
public class LogAspect {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ObjectMapper objectMapper; // Jackson 工具，用于将对象转为 JSON 字符串

    @Around("@annotation(com.jiangyue.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {

        // 1. 记录开始时间（Start Time）
        long begin = System.currentTimeMillis();

        // 2. 执行目标方法（Execute Target Method）
        Object result = joinPoint.proceed();

        // 3. 记录结束时间（End Time）
        long end = System.currentTimeMillis();

        // 计算耗时
        long costTime = end - begin;

        // 组装日志实体对象
        OperateLog operateLog = new OperateLog();

        Integer currentId = BaseContext.getCurrentId();

        operateLog.setOperateEmpId(currentId);
        operateLog.setOperateTime(LocalDateTime.now());
        operateLog.setClassName(joinPoint.getTarget().getClass().getName());
        operateLog.setMethodName(joinPoint.getSignature().getName());
        operateLog.setMethodParams(Arrays.toString(joinPoint.getArgs()));
        operateLog.setReturnValue(result != null ? result.toString() : "null");
        operateLog.setCostTime(costTime);

        operateLogMapper.insert(operateLog);

        log.info("AOP 记录操作日志: {}", operateLog);

        return result;
    }
}
