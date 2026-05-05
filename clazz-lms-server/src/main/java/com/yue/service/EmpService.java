package com.yue.service;

import com.yue.pojo.dto.EmpListQueryDTO;
import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.dto.EmpSaveDTO;
import com.yue.pojo.dto.EmpUpdateDTO;
import com.yue.pojo.entity.Emp;
import com.yue.pojo.PageResult;
import com.yue.pojo.vo.EmpInfoVO;
import com.yue.pojo.vo.EmpLoginVO;
import com.yue.pojo.vo.EmpVO;

import java.util.List;

public interface EmpService {
    /**
     * page query employee list
     * @param dto employee list query DTO contains query parameter
     * @return an employee VO
     */
    PageResult<EmpVO> page(EmpListQueryDTO dto);

    /**
     * Save new employee
     * @param empSaveDTO employee save DTO contains employee information and its experience list
     */
    void save(EmpSaveDTO empSaveDTO) throws Exception;

    /**
     * Delete employee by a list of id
     * @param ids a list of employee id
     */
    void delete(List<Long> ids);

    /**
     * Get employee information by id
     *
     * @param id employee id
     * @return an employee information VO
     */
    EmpInfoVO getInfo(Long id);

    /**
     * Update employee information
     * @param empUpdateDTO employee update DTO contains employee information and its experience list
     */
    void update(EmpUpdateDTO empUpdateDTO);

    /**
     * query all employee basic information
     * @return a list of employee basic information
     */
    List<EmpVO> getAllEmp();

    /**
     * Employee login
     * @param dto employee login DTO contains username and password
     * @return an employee login VO
     */
    EmpLoginVO login(EmpLoginDTO dto);

    /**
     * Get employee by id
     * @param id employee id
     * @return an employee entity
     */
    Emp getById(Long id);

    /**
     * Authenticate employee
     * @param dto employee login DTO contains username and password
     * @return an employee entity
     */
    Emp authenticate(EmpLoginDTO dto);
}
