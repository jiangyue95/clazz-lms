package com.yue.controller;

import com.yue.pojo.entity.OperateLog;
import com.yue.pojo.PageResult;
import com.yue.service.LogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Operation log REST controller (read-only).
 *
 * <p>Operation logs are written by the {@code @Log} AOP aspect on write
 * endpoints across the system, not by any user-facing API. This controller
 * only exposes read access to that audit trail.
 */
@Tag(
        name = "Operation Logs",
        description = "Audit trail for write operations (read-only)"
)
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
    @Operation(
            summary = "Page operation logs",
            description = "Returns the audit trail of write operations across the " +
                    "system. Note: result ordering is not currently guaranteed by " +
                    "the underlying query; explicit sorting by operate-time will " +
                    "be added in a follow-up",
            operationId = "pageOperationLogs"
    )
    @GetMapping
    public ResponseEntity<PageResult<OperateLog>> list(
            @RequestParam Integer page,
            @RequestParam Integer pageSize) {
        log.info("Query operation logs: page={}, pageSize={}", page, pageSize);
        PageResult<OperateLog> pageResult = logService.list(page, pageSize);
        return ResponseEntity.ok(pageResult);
    }
}
