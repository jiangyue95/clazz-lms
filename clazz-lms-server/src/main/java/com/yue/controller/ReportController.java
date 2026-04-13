package com.yue.controller;

import com.yue.pojo.ClazzCount;
import com.yue.pojo.JobOption;
import com.yue.pojo.Result;
import com.yue.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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
        JobOption jobOption = reportService.getEmpJobData();
        return Result.success(jobOption);
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
        log.info("统计学员班级人数");
        ClazzCount clazzCount = reportService.getStudentCountData();
        return Result.success(clazzCount);
    }

    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData() {
        log.info("统计学员学历人数");
        List<Map<String, Object>> degreeList = reportService.getStudentDegreeData();
        return Result.success(degreeList);
    }
}
