package com.yue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yue.mapper.EmpExprMapper;
import com.yue.mapper.EmpMapper;
import com.yue.pojo.dto.EmpListQueryDTO;
import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.dto.EmpSaveDTO;
import com.yue.pojo.dto.LoginInfo;
import com.yue.pojo.entity.Emp;
import com.yue.pojo.entity.EmpExpr;
import com.yue.pojo.entity.EmpLog;
import com.yue.pojo.vo.EmpInfoVO;
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

    /**
     * Page query employee list
     * @param dto employee list query DTO
     * @return employee list page result
     */
    @Override
    public PageResult<EmpVO> page(EmpListQueryDTO dto) {
        // 1. set pagination parameters: page number and page size
        PageHelper.startPage(dto.getPage(), dto.getPageSize());

        // 2. execute query to get employee list
        List<EmpVO> empList = empMapper.empList(dto);

        // 3. wrap the query result in a PageResult object
        Page<EmpVO> p = (Page<EmpVO>) empList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    /**
     * Save new employee
     * @param empSaveDTO employee save DTO contains employee information and its experience list
     * @throws Exception
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void save(EmpSaveDTO empSaveDTO) throws Exception {
        try {
            // 1.Save employee information
            Emp emp = Emp.builder()
                    .username(empSaveDTO.getUsername())
                    .name(empSaveDTO.getName())
                    .gender(empSaveDTO.getGender())
                    .job(empSaveDTO.getJob())
                    .entryDate(empSaveDTO.getEntryDate())
                    .deptId(empSaveDTO.getDeptId())
                    .phone(empSaveDTO.getPhone())
                    .salary(empSaveDTO.getSalary())
                    .image(empSaveDTO.getImage())
                    .createTime(LocalDateTime.now())
                    .updateTime(LocalDateTime.now())
                    .password("123456")
                    .build();
            empMapper.insert(emp);

            // 2. save employee work experience
            List<EmpExpr> exprList = empSaveDTO.getExprList();
            if  (!CollectionUtils.isEmpty(exprList)) {
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                empExprMapper.insertBatch(exprList);
            }
        }finally {
            // 3. record operation log in database
            EmpLog empLog = EmpLog.builder()
                    .operateTime(LocalDateTime.now())
                    .info("Add new employee：" + empSaveDTO.toString())
                    .build();
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

    /**
     * Get employee information by id
     *
     * @param id employee id
     * @return an employee information VO
     */
    @Override
    public EmpInfoVO getInfo(Integer id) {
        Emp emp = empMapper.getById(id);
        if (emp == null) {
            return null;
        }
        List<EmpExpr> exprList = empExprMapper.selectByEmpId(id);

        EmpInfoVO empInfoVO = EmpInfoVO.builder()
                .id(emp.getId())
                .username(emp.getUsername())
                .name(emp.getName())
                .gender(emp.getGender())
                .image(emp.getImage())
                .deptId(emp.getDeptId())
                .entryDate(emp.getEntryDate())
                .job(emp.getJob())
                .salary(emp.getSalary())
                .exprList(exprList)
                .createTime(emp.getCreateTime())
                .updateTime(emp.getUpdateTime())
                .build();
        return empInfoVO;
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