package com.yue.service.impl;

import com.yue.exception.BusinessRuleViolationException;
import com.yue.exception.ResourceNotFoundException;
import com.yue.mapper.DeptMapper;
import com.yue.pojo.dto.DeptSaveDTO;
import com.yue.pojo.dto.DeptUpdateDTO;
import com.yue.pojo.entity.Dept;
import com.yue.pojo.vo.DeptVO;
import com.yue.service.DeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * department service implementation
 */
@Service
@RequiredArgsConstructor
public class DeptServiceImpl implements DeptService {

    private final DeptMapper deptMapper;

    /**
     * Query all departments
     * @return all departments list
     */
    @Override
    public List<DeptVO> findAll() {
        return deptMapper.findAll()
                .stream()
                .map(this::toVO)
                .toList();
    }

    /**
     * Delete department by id
     * @param id department ID
     */
    @Override
    public void deleteById(Integer id) {
        // Check if the department exists
        Dept existing = deptMapper.getById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Cannot find department with id: " + id);
        }

        // Check if the department has employees
        Integer employeeCount = deptMapper.countByDeptId(id);
        if (employeeCount > 0) {
            throw new BusinessRuleViolationException(
                    "Cannot delete department: it still has " + employeeCount + " employees assigned"
            );
        }

        // Delete the department
        deptMapper.deleteById(id);
    }

    /**
     * add new department
     * @param deptSaveDTO new department DTO
     */
    @Override
    public DeptVO add(DeptSaveDTO deptSaveDTO) {
        // 1. add basic attributes: createTime, updateTime
        LocalDateTime now = LocalDateTime.now();
        Dept dept = Dept.builder()
                .name(deptSaveDTO.getName())
                .createTime(now)
                .updateTime(now)
                .build();

        // 2. Call insert Mapper method
        deptMapper.insert(dept);
        return toVO(dept);
    }

    /**
     * Query department info by id
     * @param id department ID
     * @return department info
     */
    @Override
    public DeptVO getById(Integer id) {
        Dept dept =  deptMapper.getById(id);
        if (dept == null) {
            throw new ResourceNotFoundException("Cannot find department with id: " + id);
        }
        return toVO(dept);
    }

    /**
     * update department info
     * @param deptUpdateDTO department info DTO
     */
    @Override
    public DeptVO update(Integer id, DeptUpdateDTO deptUpdateDTO) {
        // Check if the department exists, otherwise throws the 404
        Dept existing = deptMapper.getById(id);
        if (existing == null) {
            throw new ResourceNotFoundException("Cannot find department with id: " + id);
        }

        // Add basic attributes: updateTime
        Dept dept = Dept.builder()
                .id(id)
                .name(deptUpdateDTO.getName())
                .updateTime(LocalDateTime.now())
                .build();

        // Call update Mapper method
        deptMapper.update(dept);

        // Return the updated complete data
        return getById(id);
    }

    /**
     * Entity -> VO vonversion.
     * Centralized here so all callers produce consistent VOs.
     * @param dept a Dept object
     * @return a DeptVO object
     */
    private DeptVO toVO(Dept dept) {
        return DeptVO.builder()
                .id(dept.getId())
                .name(dept.getName())
                .createTime(dept.getCreateTime())
                .updateTime(dept.getUpdateTime())
                .build();
    }
}
