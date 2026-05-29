package com.yue.service;

import com.yue.pojo.PageResult;
import com.yue.pojo.dto.StudentSaveDTO;
import com.yue.pojo.dto.StudentUpdateDTO;
import com.yue.pojo.StudentQueryParam;
import com.yue.pojo.vo.StudentVO;

/**
 * StudentService interface.
 *
 * <p>Methods that operate on a specific student (get / update / delete /
 * recordViolation) throw {@link com.yue.exception.ResourceNotFoundException}
 * if no student with the given id exists. The exception is centrally mapped
 * to HTTP 404 by {@code GlobalExceptionHandler}, so controllers don't need
 * special handling.
 */
public interface StudentService {

    /**
     * Page-query student list.
     *
     * @param studentQueryParam query params (filters + pagination)
     * @return paged result (possibly empty, never null)
     */
    PageResult<StudentVO> page(StudentQueryParam studentQueryParam);

    /**
     * Create a new student.
     *
     * @param studentSaveDTO student creation payload
     * @return the created student (id populated by the database)
     */
    StudentVO add(StudentSaveDTO studentSaveDTO);

    /**
     * Look up a student by id.
     *
     * @param id student id
     * @return the student VO
     * @throws com.yue.exception.ResourceNotFoundException if no student with that id
     */
    StudentVO getStudentById(Integer id);

    /**
     * Update an existing student.
     *
     * <p>The {@code id} parameter is authoritative; if the DTO carries an
     * {@code id} field it is ignored.
     *
     * @param id student id (from URL path)
     * @param studentUpdateDTO update payload
     * @return the updated student
     * @throws com.yue.exception.ResourceNotFoundException if no student with that id
     */
    StudentVO modifyStudentInfo(Integer id, StudentUpdateDTO studentUpdateDTO);

    /**
     * Delete a student by id.
     *
     * @param id student id
     * @throws com.yue.exception.ResourceNotFoundException if no student with that id
     */
    void delete(Integer id);

    /**
     * Record a violation against a student. Increments {@code violation_score}
     * by the given amount and {@code violation_count} by 1, automatically.
     *
     * @param studentId student id
     * @param score violation score to add (must be positive)
     * @return the updated student (with new score and count)
     * @throws com.yue.exception.ResourceNotFoundException if no student with that id
     */
    StudentVO recordViolation(Integer studentId, Integer score);
}
