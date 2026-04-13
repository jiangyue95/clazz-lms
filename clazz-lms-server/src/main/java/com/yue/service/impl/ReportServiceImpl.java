package com.yue.service.impl;

import com.yue.mapper.EmpMapper;
import com.yue.mapper.StudentMapper;
import com.yue.pojo.ClazzCount;
import com.yue.pojo.JobOption;
import com.yue.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private StudentMapper studentMapper;

    /**
     * 获取员工职位人数
     * @return JobOption 职位数据
     */
    @Override
    public JobOption getEmpJobData() {
        // 1. 调用用 Mapper 接口，获取统计数据
        List<Map<String, Object>> list = empMapper.countEmpJobData();

        // 2. 组装结果，并返回
        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();
        return new JobOption(jobList, dataList);
    }

    /**
     * 获取员工性别人数
     * @return List<Map<String, Object>>
     */
    @Override
    public List<Map<String, Object>> getGenderData() {
        return empMapper.countEmpGenderData();
    }

    /**
     * 获取学员班级人数
     * @return ClazzCount 对象
     */
    @Override
    public ClazzCount getStudentCountData() {
        List<Map<String, Object>> list = studentMapper.countStudentClazzData();
        List<Object> clazzList = list.stream().map(dataMap -> dataMap.get("clazz_name")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();
        return new ClazzCount(clazzList, dataList);
    }

    @Override
    public List<Map<String, Object>> getStudentDegreeData() {
        return studentMapper.countStudentDegreeData();
    }
}
