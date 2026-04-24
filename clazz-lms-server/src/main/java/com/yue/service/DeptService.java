package com.yue.service;

import com.yue.pojo.dto.DeptSaveDTO;
import com.yue.pojo.dto.DeptUpdateDTO;
import com.yue.pojo.vo.DeptVO;

import java.util.List;

/**
 * department service interface
 */
public interface DeptService {
    /**
     * Query all departments
     * @return all departments list
     */
    List<DeptVO> findAll();

    /**
     * Delete department by id
     * @param id department ID
     */
    void deleteById(Integer id);

    /**
     * Add new department
     * @param deptSaveDTO new department DTO
     */
    DeptVO add(DeptSaveDTO deptSaveDTO);

    /**
     * Query department info by id
     * @param id department ID
     * @return department info
     */
    DeptVO getById(Integer id);

    /**
     * Update department info
     * @param deptUpdateDTO department info DTO
     */
    DeptVO update(Integer id, DeptUpdateDTO deptUpdateDTO);
}
