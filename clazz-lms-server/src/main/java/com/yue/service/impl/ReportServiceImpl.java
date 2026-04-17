package com.yue.service.impl;

import com.yue.mapper.EmpMapper;
import com.yue.mapper.StudentMapper;
import com.yue.pojo.vo.*;
import com.yue.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Report service implementation
 * Provide report statistics data
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private StudentMapper studentMapper;

    /**
     * Get employee job position data
     * @return a EmpJobOptionVO object
     */
    @Override
    public EmpJobOptionVO getEmpJobData() {
        List<Map<String, Object>> list = empMapper.countEmpJobData();

        List<Object> jobList = list.stream().map(dataMap -> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();
        return EmpJobOptionVO.builder().jobList(jobList).dataList(dataList).build();
    }

    /**
     * Get employee gender data
     * @return a list of employee gender vo
     */
    @Override
    public List<EmpGenderVO> getGenderData() {
        List<Map<String, Object>> rawList = empMapper.countEmpGenderData();
        return rawList.stream().map(dataMap -> EmpGenderVO.builder()
                .name((String) dataMap.get("name"))
                .value((Long) dataMap.get("value"))
                .build()).toList();
    }

    /**
     * Get clazz(class) student count data
     * @return a ClazzCountVO object
     */
    @Override
    public ClazzCountVO getStudentCountData() {
        List<Map<String, Object>> list = studentMapper.countStudentClazzData();
        List<Object> clazzList = list.stream().map(dataMap -> dataMap.get("clazz_name")).toList();
        List<Object> dataList = list.stream().map(dataMap -> dataMap.get("num")).toList();
        return ClazzCountVO.builder().clazzList(clazzList).dataList(dataList).build();
    }

    /**
     * Get student degree data
     * @return a list of student degree data vo
     */
    @Override
    public List<StudentDegreeVO> getStudentDegreeData() {
        List<Map<String, Object>> list = studentMapper.countStudentDegreeData();
        return list.stream().map(dataMap -> StudentDegreeVO.builder()
                .name((String) dataMap.get("name"))
                .value(((Long) dataMap.get("value")).intValue())
                .build()).toList();
    }
}
