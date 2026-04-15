package com.yue.service;

import com.yue.pojo.vo.ClazzCountVO;
import com.yue.pojo.vo.JobOptionVO;
import com.yue.pojo.vo.StudentDegreeVO;

import java.util.List;
import java.util.Map;

public interface ReportService {

    /**
     * 统计员工职位信息
     * @return
     */
    JobOptionVO getEmpJobData();

    /**
     * 统计员工性别信息
     * @return
     */
    List<Map<String, Object>> getGenderData();

    /**
     * 统计学生信息
     * @return
     */
    ClazzCountVO getStudentCountData();

    /**
     * 统计学生学历信息
     * @return
     */
    List<StudentDegreeVO> getStudentDegreeData();
}
