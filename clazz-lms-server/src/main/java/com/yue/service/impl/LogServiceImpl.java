package com.yue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yue.mapper.LogMapper;
import com.yue.pojo.entity.OperateLog;
import com.yue.pojo.PageResult;
import com.yue.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Log service implementation
 * */
@Service
public class LogServiceImpl implements LogService {

    /**
     * pagination query for log
     * @param page page number
     * @param pageSize page size per page
     * @return PageResult<OperateLog> object
     */
    @Autowired
    private LogMapper logMapper;
    @Override
    public PageResult<OperateLog> list(Integer page, Integer pageSize) {

        // 1. set page number and page size per page
        PageHelper.startPage(page, pageSize);

        // 2. execute query and get result list
        List<OperateLog> logList = logMapper.list();

        // 3. parse query result and wrap it in PageResult object
        Page<OperateLog> p = (Page<OperateLog>) logList;

        return new PageResult<>(p.getTotal(), p.getResult());
    }
}
