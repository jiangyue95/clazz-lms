package com.yue.controller;

import com.yue.pojo.OperateLog;
import com.yue.pojo.PageResult;
import com.yue.pojo.Result;
import com.yue.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/log")
@RestController
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping("/page")
    public Result list(Integer page, Integer pageSize) {
        log.info("分页查询: page={}, pageSize={}", page, pageSize);
        PageResult<OperateLog> pageResult = logService.list(page, pageSize);
        return Result.success(pageResult);
    }
}
