package com.yue.service;

import com.yue.pojo.entity.OperateLog;
import com.yue.pojo.PageResult;

/**
 * Log service interface
 */
public interface LogService {

    /**
     * pagination query for log
     * @param page page number
     * @param pageSize page size per page
     * @return PageResult<OperateLog> object
     */
    PageResult<OperateLog> list(Integer page, Integer pageSize);
}
