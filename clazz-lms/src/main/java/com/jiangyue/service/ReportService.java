package com.jiangyue.service;

import com.jiangyue.pojo.ClazzCount;
import com.jiangyue.pojo.JobOption;

import java.util.List;
import java.util.Map;

public interface ReportService {

    /**
     * 统计员工职位信息
     * @return
     */
    JobOption getEmpJobData();

    /**
     * 统计员工性别信息
     * @return
     */
    List<Map<String, Object>> getGenderData();

    /**
     * 统计学生信息
     * @return
     */
    ClazzCount getStudentCountData();

    /**
     * 统计学生学历信息
     * @return
     */
    List<Map<String, Object>> getStudentDegreeData();
}
