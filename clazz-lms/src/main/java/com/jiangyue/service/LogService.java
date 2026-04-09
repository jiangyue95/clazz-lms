package com.jiangyue.service;

import com.jiangyue.pojo.OperateLog;
import com.jiangyue.pojo.PageResult;

public interface LogService {

    /**
     * 分页查询出日志
     * @param page 页码
     * @param pageSize 每页数据量
     * @return PageResult<OperateLog> 对象
     */
    PageResult<OperateLog> list(Integer page, Integer pageSize);
}
