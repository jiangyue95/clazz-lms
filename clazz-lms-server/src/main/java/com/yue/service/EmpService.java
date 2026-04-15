package com.yue.service;

import com.yue.pojo.dto.EmpListQueryDTO;
import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.entity.Emp;
import com.yue.pojo.EmpQueryParam;
import com.yue.pojo.dto.LoginInfo;
import com.yue.pojo.PageResult;
import com.yue.pojo.vo.EmpLoginVO;
import com.yue.pojo.vo.EmpVO;

import java.util.List;

public interface EmpService {
    // PageResult<Emp> list(String name, Integer gender, LocalDate begin, LocalDate
    // end,Integer page, Integer pageSize);

    /**
     * 查询员工信息
     * 
     * @param empQueryParam
     * @return
     */
    PageResult<EmpVO> page(EmpListQueryDTO dto);

    /**
     * 保存员工信息
     * 
     * @param emp 员工信息
     */
    void save(Emp emp) throws Exception;

    /**
     * Delete employee by a list of id
     * @param ids a list of employee id
     */
    void delete(List<Integer> ids);

    /**
     * 根据 id 查询员工信息
     * 
     * @param id
     * @return
     */
    Emp getInfo(Integer id);

    void update(Emp emp);

    /**
     * 查询所有员工信息
     * 
     * @return 返回一个 员工信息列表
     */
    List<Emp> getAllEmp();

    /**
     * Employee login
     * @param dto
     * @return
     */
    EmpLoginVO login(EmpLoginDTO dto);
}
