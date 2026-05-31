package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.PageResult;
import com.yue.pojo.dto.StudentQueryParam;
import com.yue.pojo.dto.StudentSaveDTO;
import com.yue.pojo.dto.StudentUpdateDTO;
import com.yue.pojo.dto.ViolationDTO;
import com.yue.pojo.vo.StudentVO;
import com.yue.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Student REST controller.
 *
 * <p>Follows REST conventions: return DTOs directly via {@link ResponseEntity},
 * uses HTTP status codes for errors (delegated to GlobalExceptionHandler), and
 * leverages proper HTTP semantics (201 Created for POST, 204 No Content for
 * DELETE). The legacy {@code Result} wrapper has been removed in favour of
 * standard status-code-driven response, bringing this controller in line with
 * the rest of the codebase.
 */
@Tag(name = "Students", description = "Student management")
@Slf4j
@RequestMapping("/students")
@RestController
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    /**
     * Page-query students with optional filters.
     *
     * @param studentQueryParam filter and pagination params
     * @return 200 OK with the paged result (possibly empty)
     */
    @Operation(
            summary = "Page students with optional filters",
            operationId = "pageStudents"
    )
    @GetMapping
    public ResponseEntity<PageResult<StudentVO>> page(StudentQueryParam studentQueryParam) {
        log.info("Student list query:{}", studentQueryParam);
        PageResult<StudentVO> pageResult = studentService.page(studentQueryParam);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * Create a new student.
     *
     * @param studentSaveDTO student creation payload
     * @return 201 Created with the new resource and a Location header pointing to it
     */
    @Operation(
            summary = "Create a new student",
            operationId = "createStudent"
    )
    @Log
    @PostMapping
    public ResponseEntity<StudentVO> save(@Valid @RequestBody StudentSaveDTO studentSaveDTO) {
        log.info("Add new student:{}", studentSaveDTO);
        StudentVO created = studentService.add(studentSaveDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Get a student by id.
     *
     * @param id student id
     * @return 200 OK with the student; 404 if not found (handled centrally)
     */
    @Operation(
            summary = "Get a student by id",
            operationId = "getStudent"
    )
    @GetMapping("/{id}")
    public ResponseEntity<StudentVO> get(@PathVariable Integer id) {
        log.info("Query student by id:{}", id);
        StudentVO studentVO = studentService.getStudentById(id);
        return ResponseEntity.ok(studentVO);
    }

    /**
     * Update an existing student.
     *
     * @param id student id (from URL, authoritative)
     * @param studentUpdateDTO student update payload
     * @return 200 OK with the updated student; 404 if not found
     */
    @Operation(
            summary = "Update a student by id",
            operationId = "updateStudent"
    )
    @Log
    @PutMapping("/{id}")
    public ResponseEntity<StudentVO> modifyStudentInfo(
            @PathVariable Integer id,
            @Valid @RequestBody StudentUpdateDTO studentUpdateDTO) {
        log.info("Update student id={}, payload={}", id, studentUpdateDTO);
        StudentVO updated = studentService.modifyStudentInfo(id, studentUpdateDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a student by id.
     *
     * @param id student id
     * @return 204 No Content on success; 404 if not found
     */
    @Operation(
            summary = "Delete a student by id",
            operationId = "deleteStudent"
    )
    @Log
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Delete student by id:{}", id);
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Record a violation against a student.
     *
     * <p>Each call adds {@code score} points to the student's running total
     * and increments their violation count by one. Modelled as creating a new
     * violation record under the student resource, hence POST (non-idempotent)
     * rather than PUT.
     *
     * @param id student id
     * @param violationDTO payload carrying the violation score
     * @return 200 OK with the updated student (new score and count); 404 if not found
     */
    @Operation(
            summary = "Record a violation for a student",
            description = "Adds the given violation score to the student's " +
                    "running total and increments their violation count. " +
                    "Additive: each call records a new violation.",
            operationId = "recordStudentViolation"
    )
    @Log
    @PostMapping("/{id}/violations")
    public ResponseEntity<StudentVO> recordViolation(
            @PathVariable Integer id,
            @Valid @RequestBody ViolationDTO violationDTO) {
        log.info("Record violation for student id={}, score={}", id, violationDTO.getScore());
        StudentVO updated = studentService.recordViolation(id, violationDTO.getScore());
        return ResponseEntity.ok(updated);
    }
}
