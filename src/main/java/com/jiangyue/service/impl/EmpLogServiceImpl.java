package com.jiangyue.service.impl;

import com.jiangyue.mapper.EmpLogMapper;
import com.jiangyue.pojo.EmpLog;
import com.jiangyue.service.EmpLogService;
import com.jiangyue.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpLogServiceImpl implements EmpLogService {

    @Autowired
    private EmpLogMapper empLogMapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW) // 需要在一个新的事务中运行，不加入已有的事务
    @Override
    public void insert(EmpLog empLog) {
        empLogMapper.insert(empLog);
    }
}
