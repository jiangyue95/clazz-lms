package com.yue.mapper;

import com.yue.pojo.entity.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpExprMapper {

    /**
     * Batch insert employee work experience
     * @param exprList a list of employee work experience entity
     */
    void insertBatch(List<EmpExpr> exprList);

    /**
     * Batch delete employee work experience
     * @param empIds a list of employee id
     */
    void deleteByEmpIds(List<Integer> empIds);

    /**
     * Get employee work experience by employee id
     * @param empId employee id
     * @return a list of employee work experience
     */
    List<EmpExpr> selectByEmpId(Integer empId);
}
