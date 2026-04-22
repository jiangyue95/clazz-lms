package com.yue.controller;

import com.yue.pojo.entity.OperateLog;
import com.yue.pojo.PageResult;
import com.yue.pojo.Result;
import com.yue.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Log controller
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/log")
public class LogController {

    private final LogService logService;

    /**
     * pagination query for log
     * @param page page number
     * @param pageSize page size per page
     * @return PageResult<OperateLog> object
     */
    @GetMapping("/page")
    public Result list(Integer page, Integer pageSize) {
        log.info("分页查询: page={}, pageSize={}", page, pageSize);
        PageResult<OperateLog> pageResult = logService.list(page, pageSize);
        return Result.success(pageResult);
    }
}
