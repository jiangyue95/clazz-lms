package com.yue.controller;

import com.yue.pojo.entity.OperateLog;
import com.yue.pojo.PageResult;
import com.yue.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Operation log REST controller (read-only).
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/logs")
public class LogController {

    private final LogService logService;

    /**
     * Pagination query of operation logs.
     *
     * @param page 1-based page number
     * @param pageSize items per page
     * @return 200 OK with the paged result
     */
    @GetMapping
    public ResponseEntity<PageResult<OperateLog>> list(
            @RequestParam Integer page,
            @RequestParam Integer pageSize) {
        log.info("Query operation logs: page={}, pageSize={}", page, pageSize);
        PageResult<OperateLog> pageResult = logService.list(page, pageSize);
        return ResponseEntity.ok(pageResult);
    }
}
