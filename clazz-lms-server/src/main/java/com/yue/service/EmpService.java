package com.yue.service;

import com.yue.pojo.Emp;
import com.yue.pojo.EmpQueryParam;
import com.yue.pojo.LoginInfo;
import com.yue.pojo.PageResult;

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
    PageResult<Emp> page(EmpQueryParam empQueryParam);

    /**
     * 保存员工信息
     * 
     * @param emp 员工信息
     */
    void save(Emp emp) throws Exception;

    /**
     * 批量删除员工信息
     * 
     * @param ids
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
     * 登录
     * @param emp 请求体中的员工信息
     * @return 一个 LoginInfo 对象
     */
    LoginInfo login(Emp emp);
}
