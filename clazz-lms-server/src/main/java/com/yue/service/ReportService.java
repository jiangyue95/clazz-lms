package com.yue.service;

import com.yue.pojo.vo.*;

import java.util.List;

public interface ReportService {

    /**
     * 统计员工职位信息
     * @return
     */
    EmpJobOptionVO getEmpJobData();

    /**
     * Get employee gender data
     * @return a list of employee gender vo
     */
    List<EmpGenderVO> getGenderData();

    /**
     * Get clazz(class) student count data
     * @return a ClassCountVO object
     */
    ClazzCountVO getStudentCountData();

    /**
     * Get student degree data
     * @return a list of StudentDegreeVO objects
     */
    List<StudentDegreeVO> getStudentDegreeData();
}
