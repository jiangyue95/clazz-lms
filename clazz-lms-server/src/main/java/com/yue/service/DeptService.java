package com.yue.service;

import com.yue.pojo.dto.DeptSaveDTO;
import com.yue.pojo.dto.DeptUpdateDTO;
import com.yue.pojo.entity.Dept;

import java.util.List;

/**
 * department service interface
 */
public interface DeptService {
    /**
     * Query all departments
     * @return all departments list
     */
    List<Dept> findAll();

    /**
     * Delete department by id
     * @param id department ID
     */
    void deleteById(Integer id);

    /**
     * Add new department
     * @param deptSaveDTO new department DTO
     */
    void add(DeptSaveDTO deptSaveDTO);

    /**
     * Query department info by id
     * @param id department ID
     * @return department info
     */
    Dept getById(Integer id);

    /**
     * Update department info
     * @param deptUpdateDTO department info DTO
     */
    void update(DeptUpdateDTO deptUpdateDTO);
}
