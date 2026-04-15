package com.yue.service.impl;

import com.yue.mapper.EmpMapper;
import com.yue.mapper.StudentMapper;
import com.yue.pojo.vo.ClazzCountVO;
import com.yue.pojo.vo.JobOptionVO;
import com.yue.pojo.vo.StudentDegreeVO;
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
     * @return JobOptionVO 职位数据
     */
    @Override
    public JobOptionVO getEmpJobData() {
        // 1. 调用用 Mapper 接口，获取统计数据
        List<Map<String, Object>> list = empMapper.countEmpJobData();

        // 2. 组装结果，并返回
        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();
        return JobOptionVO.builder().jobList(jobList).dataList(dataList).build();
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
     * @return ClazzCountVO 对象
     */
    @Override
    public ClazzCountVO getStudentCountData() {
        List<Map<String, Object>> list = studentMapper.countStudentClazzData();
        List<Object> clazzList = list.stream().map(dataMap -> dataMap.get("clazz_name")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();
        return ClazzCountVO.builder().clazzList(clazzList).dataList(dataList).build();
    }

    @Override
    public List<StudentDegreeVO> getStudentDegreeData() {
        return studentMapper.countStudentDegreeData();
    }
}
