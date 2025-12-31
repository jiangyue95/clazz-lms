package com.jiangyue.service.impl;

import com.jiangyue.exception.DemptHasEmpException;
import com.jiangyue.mapper.DeptMapper;
import com.jiangyue.pojo.Dept;
import com.jiangyue.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    /**
     * 根据 ID 删除部门
     */
    @Override
    public void deleteById(Integer id) {
        // 查询部门人数
        Integer studentCount = deptMapper.countByDeptId(id);
        if (studentCount > 0) {
            throw new DemptHasEmpException("对不起, 该部门下有员工, 不能直接删除");
        }
        deptMapper.deleteById(id);
    }

    @Override
    public void add(Dept dept) {
        // 1. 补全基础属性 - createTime，updateTime
        dept.setCreateTime(LocalDateTime.now());
        dept.setUpdateTime(LocalDateTime.now());

        // 2. 调用 Mapper 接口方法插入数据
        deptMapper.insert(dept);
    }

    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }

    @Override
    public void update(Dept dept) {
        // 补全基础属性
        dept.setUpdateTime(LocalDateTime.now());
        // 调用 Mapper 接口方法更新数据
        deptMapper.update(dept);
    }

}
