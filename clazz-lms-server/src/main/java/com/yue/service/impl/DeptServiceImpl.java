package com.yue.service.impl;

import com.yue.exception.BusinessRuleViolationException;
import com.yue.mapper.DeptMapper;
import com.yue.pojo.dto.DeptSaveDTO;
import com.yue.pojo.dto.DeptUpdateDTO;
import com.yue.pojo.entity.Dept;
import com.yue.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * department service implementation
 */
@Service
public class DeptServiceImpl implements DeptService {

    @Autowired
    private DeptMapper deptMapper;

    /**
     * Query all departments
     * @return all departments list
     */
    @Override
    public List<Dept> findAll() {
        return deptMapper.findAll();
    }

    /**
     * Delete department by id
     * @param id department ID
     */
    @Override
    public void deleteById(Integer id) {
        // 1. check if the department has employees
        Integer employeeCount = deptMapper.countByDeptId(id);
        if (employeeCount > 0) {
            throw new BusinessRuleViolationException("Cannot delete department: it still has employees assigned");
        }

        // 2. delete the department
        deptMapper.deleteById(id);
    }

    /**
     * add new department
     * @param deptSaveDTO new department DTO
     */
    @Override
    public void add(DeptSaveDTO deptSaveDTO) {
        // 1. add basic attributes: createTime, updateTime
        Dept dept = Dept.builder()
                .name(deptSaveDTO.getName())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        // 2. Call insert Mapper method
        deptMapper.insert(dept);
    }

    /**
     * Query department info by id
     * @param id department ID
     * @return department info
     */
    @Override
    public Dept getById(Integer id) {
        return deptMapper.getById(id);
    }

    /**
     * update department info
     * @param deptUpdateDTO department info DTO
     */
    @Override
    public void update(DeptUpdateDTO deptUpdateDTO) {
        // add basic attributes: updateTime
        Dept dept = Dept.builder()
                .id(deptUpdateDTO.getId())
                .name(deptUpdateDTO.getName())
                .updateTime(LocalDateTime.now())
                .build();
        // call update Mapper method
        deptMapper.update(dept);
    }
}
