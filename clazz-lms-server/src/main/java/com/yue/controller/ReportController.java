package com.yue.controller;

import com.yue.pojo.vo.ClazzCountVO;
import com.yue.pojo.vo.EmpGenderVO;
import com.yue.pojo.vo.EmpJobOptionVO;
import com.yue.pojo.vo.StudentDegreeVO;
import com.yue.pojo.Result;
import com.yue.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Report controller
 * Provide report statistics data
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    /**
     * Get employee job data
     * @return a result contains an EmpJobOptionVO object
     */
    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("Employee job position data");
        EmpJobOptionVO empJobOptionVO = reportService.getEmpJobData();
        return Result.success(empJobOptionVO);
    }

    /**
     * Get employee gender data
     * @return a result contains a list of EmpGenderVO objects
     */
    @GetMapping("/empGenderData")
    public Result getGenderData() {
        log.info("Get employee gender data");
        List<EmpGenderVO> genderList = reportService.getGenderData();
        return Result.success(genderList);
    }

    /**
     * Get clazz(class) student count data
     * @return a Result object contains a ClassCountVO object
     */
    @GetMapping("/studentCountData")
    public Result getStudentCountData() {
        log.info("Number of students in the class");
        ClazzCountVO clazzCountVO = reportService.getStudentCountData();
        return Result.success(clazzCountVO);
    }

    /**
     * Get student degree data
     * @return a result contains a list of StudentDegreeVO objects
     */
    @GetMapping("/studentDegreeData")
    public Result getStudentDegreeData() {
        log.info("Number of students with academic qualifications");
        List<StudentDegreeVO> degreeList = reportService.getStudentDegreeData();
        return Result.success(degreeList);
    }
}
