package com.yue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yue.exception.InvalidCredentialsException;
import com.yue.mapper.EmpExprMapper;
import com.yue.mapper.EmpMapper;
import com.yue.pojo.dto.EmpListQueryDTO;
import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.dto.EmpSaveDTO;
import com.yue.pojo.dto.EmpUpdateDTO;
import com.yue.pojo.entity.Emp;
import com.yue.pojo.entity.EmpExpr;
import com.yue.pojo.entity.EmpLog;
import com.yue.pojo.vo.EmpInfoVO;
import com.yue.pojo.vo.EmpLoginVO;
import com.yue.pojo.vo.EmpVO;
import com.yue.service.EmpLogService;
import com.yue.service.EmpService;
import com.yue.security.JwtService;
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

    @Autowired
    private JwtService jwtService;

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

    /**
     * Update employee information
     * @param empUpdateDTO employee update DTO contains employee information and its experience list
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(EmpUpdateDTO empUpdateDTO) {
        // 1. check if the employee exists or not
        Emp existingEmp = empMapper.getById(empUpdateDTO.getId());
        if (existingEmp == null) {
            throw new RuntimeException("Employee not found: " + empUpdateDTO.getId());
        }

        // 2. update employee basic information
        Emp emp = Emp.builder()
                .id(existingEmp.getId())
                .username(empUpdateDTO.getUsername())
                .name(empUpdateDTO.getName())
                .gender(empUpdateDTO.getGender())
                .job(empUpdateDTO.getJob())
                .entryDate(empUpdateDTO.getEntryDate())
                .deptId(empUpdateDTO.getDeptId())
                .phone(empUpdateDTO.getPhone())
                .salary(empUpdateDTO.getSalary())
                .image(empUpdateDTO.getImage())
                .updateTime(LocalDateTime.now())
                .build();
        empMapper.updateById(emp);

        // 3. delete the old work experience
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));

        // 4. insert new work experience into database
        List<EmpExpr> exprList = empUpdateDTO.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> empExpr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    /**
     * query all employee basic information
     * @return a list of employee basic information
     */
    @Override
    public List<EmpVO> getAllEmp() {
        return empMapper.queryAll();
    }

    /**
     * Login employee by username and password
     *
     * @param dto login request body
     * @return employee login vo with JWT token
     * @throws InvalidCredentialsException if username doesn't exist or password is wrong
     */
    @Override
    public EmpLoginVO login(EmpLoginDTO dto) {
        // 1. select employee by username
        Emp emp = empMapper.getByUsername(dto.getUsername());

        // 2. check credentials
        //    Note: log the precise reason internally for debugging,
        //    but expose only a vague message externally to prevent
        //    user enumeration attacks.
        if (emp == null) {
            log.warn("Login failed: username not found: {}", dto.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }
        if (!emp.getPassword().equals(dto.getPassword())) {
            log.warn("Login failed: incorrect password for username: {}", dto.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // 3. generate JWT token
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", emp.getId());
        claims.put("username", emp.getUsername());
        String token = jwtService.generateToken(claims);

        // 4. construct and return vo
        return EmpLoginVO.builder()
                .id(emp.getId())
                .username(emp.getUsername())
                .name(emp.getName())
                .token(token)
                .build();
    }
}