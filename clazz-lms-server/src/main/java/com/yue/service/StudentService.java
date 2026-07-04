package com.yue.service;

import com.yue.exception.ForbiddenException;
import com.yue.exception.ResourceNotFoundException;
import com.yue.pojo.PageResult;
import com.yue.pojo.dto.StudentQueryParam;
import com.yue.pojo.dto.StudentSaveDTO;
import com.yue.pojo.dto.StudentUpdateDTO;
import com.yue.pojo.vo.StudentVO;

/**
 * StudentService interface.
 *
 * <p>Methods that operate on a specific student (get / update / delete /
 * recordViolation) accept a {@code scopeMasterId} that narrows access to
 * a head teacher's own class. They throw {@link ResourceNotFoundException}
 * when no student matches the id within the given scope - whether genuinely
 * missing or out of scope, the two are indistinguishable to the caller by
 * design (avoids leaking resource existence). A null scopeMasterId applies
 * no restriction (admin path).
 */
public interface StudentService {

    /**
     * Page-query student list.
     *
     * @param studentQueryParam query params (filters + pagination)
     * @param scopeMasterId ownership scope; when non-null, results are
     *                      narrowed to students in the caller's own class;
     *                      null applies no restriction (admin path)
     * @return paged result (possibly empty, never null)
     */
    PageResult<StudentVO> page(StudentQueryParam studentQueryParam, Integer scopeMasterId);

    /**
     * Create a new student.
     *
     * @param studentSaveDTO student creation payload
     * @param scopeMasterId ownership scope. If non-null (head teacher), the new
     *                      student's target class must be owned by this master_id,
     *                      otherwise creation is rejected. If null (admin), no
     *                      ownership restriction is applied.
     * @return the created student (id populated by the database)
     * @throws ForbiddenException if a head teacher attempts to create a student in a
     *         class they do not own (or a class that does not exist)
     */
    StudentVO add(StudentSaveDTO studentSaveDTO, Integer scopeMasterId);

    /**
     * Look up a single student by id, optionally restricted to a head
     * teacher's own class.
     *
     * @param id            student id
     * @param scopeMasterId ownership scope. If non-null, only a student whose
     *                      class {@code master_id} equals this value is returned;
     *                      a student outside that scope is treated as not found.
     *                      If null, no ownership restriction is applied (admin path).
     * @return the matching student VO
     * @throws ResourceNotFoundException if no student matches
     *         the id within the given scope (genuinely missing, or out of scope)
     */
    StudentVO getStudentById(Integer id, Integer scopeMasterId);

    /**
     * Update an existing student.
     *
     * <p>The {@code id} parameter is authoritative; if the DTO carries an
     * {@code id} field it is ignored.
     *
     * @param id student id (from URL path)
     * @param studentUpdateDTO update payload
     * @param scopeMasterId ownership scope; null means no restriction (admin path)
     * @return the updated student
     * @throws ResourceNotFoundException if no student matches the id within the
     *         given scope (genuinely missing, or out of scope)
     */
    StudentVO modifyStudentInfo(Integer id, StudentUpdateDTO studentUpdateDTO, Integer scopeMasterId);

    /**
     * Delete a student by id.
     *
     * @param id            student id
     * @param scopeMasterId ownership scope; null means no restriction (admin path)
     * @throws ResourceNotFoundException if no student matches the id within the
     *         given scope (genuinely missing, or out of scope)
     */
    void delete(Integer id, Integer scopeMasterId);

    /**
     * Record a violation against a student. Increments {@code violation_score}
     * by the given amount and {@code violation_count} by 1, automatically.
     *
     * @param studentId student id
     * @param score violation score to add (must be positive)
     * @param scopeMasterId ownership scope; null means no restriction (admin path)
     * @return the updated student (with new score and count)
     * @throws ResourceNotFoundException if no student matches the id within the
     *         given scope (genuinely missing, or out of scope)
     */
    StudentVO recordViolation(Integer studentId, Integer score, Integer scopeMasterId);
}
