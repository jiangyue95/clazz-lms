package com.yue.controller;

import com.yue.pojo.vo.ClazzCountVO;
import com.yue.pojo.vo.JobOptionVO;
import com.yue.pojo.vo.StudentDegreeVO;
import com.yue.pojo.Result;
import com.yue.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Report controller
 * Provide report statistics data
 */
@Slf4j
@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 统计员工职位人数
     * @return
     */
    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("统计员工职位人数");
        JobOptionVO jobOptionVO = reportService.getEmpJobData();
        return Result.success(jobOptionVO);
    }

    /**
     * 统计员工性别人数
     * @return
     */
    @GetMapping("/empGenderData")
    public Result getGenderData() {
        log.info("统计员工性别人数");
        List<Map<String, Object>> genderList = reportService.getGenderData();
        return Result.success(genderList);
    }

    /**
     * 统计学员班级人数
     * @return
     */
    @GetMapping("/studentCountData")
    public Result getStudentCountData() {
        log.info("Number of students in the class");
        ClazzCountVO clazzCountVO = reportService.getStudentCountData();
        return Result.success(clazzCountVO);
    }

    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData() {
        log.info("Number of students with academic qualifications");
        List<StudentDegreeVO> degreeList = reportService.getStudentDegreeData();
        return Result.success(degreeList);
    }
}
