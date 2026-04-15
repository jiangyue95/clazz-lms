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

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;
    @Override
    public PageResult<OperateLog> list(Integer page, Integer pageSize) {
        // 1. 设置分页参数（PageHelper)
        PageHelper.startPage(page, pageSize);
        // 2. 执行查询
        List<OperateLog> logList = logMapper.list();
        // 3. 解析查询结果，并封装
        Page<OperateLog> p = (Page<OperateLog>) logList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }
}
