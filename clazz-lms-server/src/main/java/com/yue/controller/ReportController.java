package com.yue.controller;

import com.yue.pojo.vo.ClazzCountVO;
import com.yue.pojo.vo.EmpGenderVO;
import com.yue.pojo.vo.EmpJobOptionVO;
import com.yue.pojo.vo.StudentDegreeVO;
import com.yue.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Report REST controller - provides aggregate / statistics endpoints
 *
 * <p>Note: thest endpoints are RPC-flavoured ("get this specific calculation")
 * rather than purely RESTful resource operations, which is acceptable for
 * reports / analytics. They still return resources directly via ResponseEntity
 * for shape consistency with the rest of the API.
 */
@Tag(
        name = "Reports",
        description = "Aggregate statistics for the admin dashboard"
)
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    /**
     * @return 200 OK with employee job-position breakdown
     */
    @Operation(
            summary = "Employee count grounded by job role",
            operationId = "getEmpJobData"
    )
    @GetMapping("/empJobData")
    public ResponseEntity<EmpJobOptionVO> getEmpJobData(){
        log.info("Get employee job position data");
        EmpJobOptionVO empJobOptionVO = reportService.getEmpJobData();
        return ResponseEntity.ok(empJobOptionVO);
    }

    /**
     * @return 200 OK with employee gender breakdown
     */
    @Operation(
            summary = "Employee count grouped by gender",
            operationId = "getEmpGenderData"
    )
    @GetMapping("/empGenderData")
    public ResponseEntity<List<EmpGenderVO>> getGenderData() {
        log.info("Get employee gender data");
        List<EmpGenderVO> genderList = reportService.getGenderData();
        return ResponseEntity.ok(genderList);
    }

    /**
     * @return 200 OK with per-clazz(class) student count
     */
    @Operation(
            summary = "Student count per class",
            operationId = "getStudentCountData"
    )
    @GetMapping("/studentCountData")
    public ResponseEntity<ClazzCountVO> getStudentCountData() {
        log.info("Number of students in the class");
        ClazzCountVO clazzCountVO = reportService.getStudentCountData();
        return ResponseEntity.ok(clazzCountVO);
    }

    /**
     * @return 200 OK with student degree breakdown
     */
    @Operation(
            summary = "Student count grouped by degree (academic qualification)",
            operationId = "getStudentDegreeData"
    )
    @GetMapping("/studentDegreeData")
    public ResponseEntity<List<StudentDegreeVO>> getStudentDegreeData() {
        log.info("Number of students with academic qualifications");
        List<StudentDegreeVO> degreeList = reportService.getStudentDegreeData();
        return ResponseEntity.ok(degreeList);
    }
}
