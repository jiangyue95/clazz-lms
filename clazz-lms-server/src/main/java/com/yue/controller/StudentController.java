package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.PageResult;
import com.yue.pojo.Result;
import com.yue.pojo.dto.StudentSaveDTO;
import com.yue.pojo.dto.StudentUpdateDTO;
import com.yue.pojo.StudentQueryParam;
import com.yue.pojo.vo.StudentVO;
import com.yue.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Student REST controller.
 *
 * <p><b>Note:</b> This controller currently returns the legacy {@link Result}
 * wrapper rather than DTOs directly, unlike the other controllers in this
 * codebase. Migration to standard REST conventions is tracked in a seperate
 * follow-up PR. OpenAPI annotations have been added here to document the
 * current state; response schemas will improve once the wrapper is removed.
 */
@Tag(name = "Students", description = "Student management")
@Slf4j
@RequestMapping("/students")
@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * Query student list based on query params.
     *
     * @param studentQueryParam query params
     * @return Result wrapping a PageResult of StudentVO
     */
    @Operation(
            summary = "Page students with optional filters",
            operationId = "pageStudents"
    )
    @GetMapping
    public Result page(StudentQueryParam studentQueryParam) {
        log.info("Student list query：{}", studentQueryParam);
        PageResult<StudentVO> pageResult = studentService.page(studentQueryParam);
        return Result.success(pageResult);
    }

    /**
     * Add new student.
     *
     * @param studentSaveDTO student creation payload
     * @return Result indicating success
     */
    @Operation(
            summary = "Create a new student",
            operationId = "createStudent"
    )
    @Log
    @PostMapping
    public Result save(@RequestBody StudentSaveDTO studentSaveDTO) {
        log.info("Add new student：{}", studentSaveDTO);
        studentService.add(studentSaveDTO);
        return Result.success();
    }

    /**
     * Query a student by id.
     *
     * @param id student id
     * @return Result wrapping the StudentVO
     */
    @Operation(
            summary = "Get a student by id",
            operationId = "getStudent"
    )
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        log.info("Query student by id：{}", id);
        StudentVO studentVO = studentService.getStudentById(id);
        return Result.success(studentVO);
    }

    /**
     * Update student information.
     *
     * <p>Note: The target student's id is taken from the request body
     * {@code StudentUpdateDTO.id} rather than the URL path. A follow-up PR
     * will migrate this to {@code PUT /students/{id}} for explicitness.
     *
     * @param studentUpdateDTO student update payload (id is inside the body)
     * @return Result indicating success
     */
    @Operation(
            summary = "Update a student",
            description = "Updates the student identified by `id` inside the " +
                    "request body. This will be migrated to PUT /students/{id} " +
                    "in a follow-up refactor.",
            operationId = "updateStudent"
    )
    @Log
    @PutMapping
    public Result modifyStudentInfo(@RequestBody StudentUpdateDTO studentUpdateDTO) {
        log.info("Update student info：{}", studentUpdateDTO);
        studentService.modifyStudentInfo(studentUpdateDTO);
        return Result.success();
    }

    /**
     * Delete a student by id.
     *
     * @param id student id
     * @return Result indicating success
     */
    @Operation(
            summary = "Delete a student by id",
            operationId = "deleteStudent"
    )
    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("Delete student by id：{}", id);
        studentService.delete(id);
        return Result.success();
    }

    /**
     * Record a violation score against a student.
     *
     * @param id student id
     * @param score violation score to record
     * @return Result indicating success
     */
    @Operation(
            summary = "Record a violation score for a student",
            description = "Records `score` violation points against the student " +
                    "identified by `id`. The score is passed as a URL path" +
                    "segment; a follow-up refactor may move it to the request " +
                    "body to better separate identification from payload.",
            operationId = "updateStudentViolationScore"
    )
    @Log
    @PutMapping("/violation/{id}/{score}")
    public Result modifyViolationScore(@PathVariable Integer id, @PathVariable Integer score) {
        log.info("Violation operation: {}, {}", id, score);
        studentService.modifyViolationScore(id, score);
        return Result.success();
    }
}
