package com.jiangyue.mapper;

import com.jiangyue.pojo.EmpExpr;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EmpExprMapper {


    void insertBatch(List<EmpExpr> exprList);

    /**
     * 根据员工 ID 批量删除员工工作经历
     * @param empIds
     */
    void delteByEmpIds(List<Integer> empIds);
}
