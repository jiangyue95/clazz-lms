package com.jiangyue.mapper;

import com.jiangyue.pojo.OperateLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {

    @Select("SELECT o.*, e.name as operateEmpName FROM operate_log AS o LEFT JOIN emp AS e on o.operate_emp_id = e.id")
    public List<OperateLog> list();
}
