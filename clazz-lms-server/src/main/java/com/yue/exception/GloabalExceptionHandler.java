package com.yue.exception;

import com.yue.pojo.Result;
import com.yue.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler class
 */
@Slf4j
@RestControllerAdvice
public class GloabalExceptionHandler {

    private final ReportService reportService;

    public GloabalExceptionHandler(ReportService reportService) {
        this.reportService = reportService;
    }

    @ExceptionHandler
    public Result handleException(Exception e) {
        log.error("程序出错了", e);
        return Result.error("出错了，请联系管理员");
    }

    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e) {
        log.error("程序出错了", e);
        String message = e.getMessage();
        int i = message.indexOf("Duplicate entry");
        String errorMsg = message.substring(i);
        String arr[] = errorMsg.split(" ");
        return Result.error(arr[2] + "已存在");
    }

    @ExceptionHandler(ClazzHasStudentException.class)
    public Result handleClazzHasStudentException(ClazzHasStudentException e) {
        log.info("班级还有学生异常", e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(DemptHasEmpException.class)
    public Result handleDemptHasEmpException(DemptHasEmpException e) {
        log.info("部门还有员工异常", e);
        return Result.error(e.getMessage());
    }
}
