package com.jiangyue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jiangyue.mapper.EmpExprMapper;
import com.jiangyue.mapper.EmpMapper;
import com.jiangyue.pojo.*;
import com.jiangyue.service.EmpLogService;
import com.jiangyue.service.EmpService;
import com.jiangyue.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmpServiceImpl implements EmpService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;
    @Autowired
    private EmpLogService empLogService;

//    @Override
//    public PageResult<Emp> list(Integer page, Integer pageSize) {
        // 原始的分页查询操作
        // 1. 获取总记录数
//        Long total = empMapper.count();
        // 2. 获取数据列表
//        Integer start = (page - 1) * pageSize;
//        List<Emp> rows = empMapper.list(start, pageSize);
        // 3. 封装分页结果
//        return new PageResult<Emp>(total, rows);
//    }

    /**
     *
     * @param name
     * @param gender
     * @param begin
     * @param end
     * @param page     页码
     * @param pageSize 每页显示的记录数
     * @return
     *
     */
//    @Override
//    public PageResult<Emp> page(String name, Integer gender, LocalDate begin, LocalDate end, Integer page, Integer pageSize) {
//        // 1. 设置分页参数（PageHelper)
//        PageHelper.startPage(page, pageSize);
//        // 2. 执行查询
//        List<Emp> empList = empMapper.list(name, gender, begin, end);
//        // 3. 解析查询结果，并封装
//        Page<Emp> p = (Page<Emp>) empList;
//        return new PageResult<>(p.getTotal(), p.getResult());
//    }

    /**
     *
     * @param empQueryParam
     * @return
     */
    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        // 1. 设置分页参数（PageHelper)
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());
        // 2. 执行查询
        List<Emp> empList = empMapper.list(empQueryParam);
        // 3. 解析查询结果，并封装
        Page<Emp> p = (Page<Emp>) empList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void save(Emp emp) throws Exception {
        try {
            // 1. 保存员工基本信息
            emp.setCreateTime(LocalDateTime.now());
            emp.setUpdateTime(LocalDateTime.now());
            empMapper.insert(emp);

            // 人工增加异常
//            int i = 1/0;

            // 2. 保存员工的工作经历
            List<EmpExpr> exprList = emp.getExprList();
            if (!CollectionUtils.isEmpty(exprList)) {
                // 遍历集合，为 empId 赋值
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                empExprMapper.insertBatch(exprList);
            }
        }finally {
            // 3. 记录操作日志
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "新增员工：" + emp.toString());
            empLogService.insert(empLog);
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void delete(List<Integer> ids) {
        // 1. 批量删除员工基本信息
        empMapper.deletByIds(ids);

        // 2. 批量删除员工的工作经历信息
        empExprMapper.delteByEmpIds(ids);
    }

    @Override
    public Emp getInfo(Integer id) {
        return empMapper.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        // 1. 根据 ID 修改员工的基本信息
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.updateById(emp);

        // 2. 根据 ID 修改员工的工作经历
        // 2.1 先根据员工 ID 删除原有的工作经历
        empExprMapper.delteByEmpIds(Arrays.asList(emp.getId()));

        // 2.2 再添加这个员工新的工作经历
        List<EmpExpr> exprList = emp.getExprList();
        if(!CollectionUtils.isEmpty(exprList)){
            // 为 expr 的 emp_id 赋值
            exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    /**
     * 查询所有员工信息
     * @return 返回一列表，包含所有员工信息
     */
    @Override
    public List<Emp> getAllEmp() {
        List<Emp> allEmps = empMapper.queryAll();
        return allEmps;
    }

    /**
     * 验证登录信息并生成 token
     * @param emp 请求体中的员工信息
     * @return
     */
    @Override
    public LoginInfo login(Emp emp) {
        // 1. 调用 mapper 接口，根据用户名和密码查询员工信息
        Emp e = empMapper.selectByUsernameAndPassword(emp);
        // 2. 判断：判断登录信息是否正确，如果正确，则生成 token，如果不成功，则返回 null
        if (e!= null) {
            log.info("登录成功，员工信息: {}", e);
            // 生成 JWT Token
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("username", e.getUsername());
            String jwt = JWTUtils.generateJWTToken(claims);
            return new LoginInfo(e.getId(), e.getUsername(), e.getName(), jwt);
        }
        return null;
    }
}