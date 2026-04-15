package com.yue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yue.mapper.EmpExprMapper;
import com.yue.mapper.EmpMapper;
import com.yue.pojo.dto.EmpListQueryDTO;
import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.dto.LoginInfo;
import com.yue.pojo.entity.Emp;
import com.yue.pojo.entity.EmpExpr;
import com.yue.pojo.entity.EmpLog;
import com.yue.pojo.vo.EmpLoginVO;
import com.yue.pojo.vo.EmpVO;
import com.yue.service.EmpLogService;
import com.yue.service.EmpService;
import com.yue.utils.JWTUtils;
import com.yue.pojo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.ResourcePatternResolver;
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
    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

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
    public PageResult<EmpVO> page(EmpListQueryDTO dto) {
        // 1. 设置分页参数（PageHelper)
        PageHelper.startPage(dto.getPage(), dto.getPageSize());
        // 2. 执行查询
        List<EmpVO> empList = empMapper.empList(dto);
        // 3. 解析查询结果，并封装
        Page<EmpVO> p = (Page<EmpVO>) empList;
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
//            List<EmpExpr> exprList = emp.getExprList();
//            if (!CollectionUtils.isEmpty(exprList)) {
//                // 遍历集合，为 empId 赋值
//                exprList.forEach(empExpr -> {
//                    empExpr.setEmpId(emp.getId());
//                });
//                empExprMapper.insertBatch(exprList);
//            }
        }finally {
            // 3. 记录操作日志
            EmpLog empLog = new EmpLog(null, LocalDateTime.now(), "新增员工：" + emp.toString());
            empLogService.insert(empLog);
        }
    }

    /**
     * Delete employee by a list of id
     * @param ids a list of employee id
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void delete(List<Integer> ids) {
        // 1. Batch delete employee basic information
        empMapper.deleteByIds(ids);

        // 2. Batch delete employee work experience
        empExprMapper.deleteByEmpIds(ids);
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
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));

        // 2.2 再添加这个员工新的工作经历
//        List<EmpExpr> exprList = emp.getExprList();
//        if(!CollectionUtils.isEmpty(exprList)){
//            // 为 expr 的 emp_id 赋值
//            exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
//            empExprMapper.insertBatch(exprList);
//        }
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
     * Login employee by username and password
     * @param dto login request body
     * @return employee login vo, or null if login failed
     */
    @Override
    public EmpLoginVO login(EmpLoginDTO dto) {
        // 1. select employee by username
        Emp emp = empMapper.getByUsername(dto.getUsername());

        // 2. check if employee exists or password is correct
        if (emp == null || !emp.getPassword().equals(dto.getPassword())) {
            return null;
        }

        // 3. generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", emp.getId());
        claims.put("username", emp.getUsername());
        String token = JWTUtils.generateJWTToken(claims);

        // 4. construct and return vo
        return EmpLoginVO.builder()
                .id(emp.getId())
                .username(emp.getUsername())
                .name(emp.getName())
                .token(token)
                .build();
    }
}