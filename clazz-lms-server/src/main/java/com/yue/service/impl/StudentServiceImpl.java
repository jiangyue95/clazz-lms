package com.yue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yue.exception.ForbiddenException;
import com.yue.exception.ResourceNotFoundException;
import com.yue.mapper.ClazzMapper;
import com.yue.mapper.StudentMapper;
import com.yue.pojo.PageResult;
import com.yue.pojo.dto.StudentQueryParam;
import com.yue.pojo.dto.StudentSaveDTO;
import com.yue.pojo.dto.StudentUpdateDTO;
import com.yue.pojo.entity.Student;
import com.yue.pojo.vo.ClazzVO;
import com.yue.pojo.vo.StudentVO;
import com.yue.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * StudentService implementation.
 *
 * <p>Ownership and existence are both enforced in the persistence layer:
 * scoped queries / scoped writes mean an out-of scope or missing row is
 * indistinguishable (affected == 0 or null -> 404), keeping authorization
 * checks out of the controllers and in one place.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;
    private final ClazzMapper clazzMapper;

    /**
     * {@inheritDoc}
     *
     * <p>Ownership is pushed into the SQL WHERE clause: a non-null scopeMasterId
     * narrows the result set (and the PageHelper count) to the caller's own
     * class, so an out-of-scope caller simply sees fewer rows - not a 404.
     */
    @Override
    public PageResult<StudentVO> page(StudentQueryParam studentQueryParam, Integer scopeMasterId) {
        // PageHelper intercepts the very next MyBatis query on this thread
        // and applies LIMIT/OFFSET, then exposes total count via the Page wrapper.
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        List<StudentVO> studentList = studentMapper.list(studentQueryParam, scopeMasterId);
        Page<StudentVO> p = (Page<StudentVO>) studentList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    /**
     * {@inheritDoc}
     *
     * <p>For a head teacher (non-null scopeMasterId), the target class is loaded
     * and its master_id checked before insert; a class that is missing or not
     * owned is rejected with 403. Admins (null) skip the check. The generated key
     * is backfilled by MyBatis, then the full VO is re-fetched.
     */
    @Override
    @Transactional
    public StudentVO add(StudentSaveDTO studentSaveDTO, Integer scopeMasterId) {
        if (scopeMasterId != null) {
            ClazzVO clazz = clazzMapper.selectById(studentSaveDTO.getClazzId());
            if (clazz == null || !scopeMasterId.equals(clazz.getMasterId())) {
                throw new ForbiddenException("Cannot create a student in a class you do not own");
            }
        }
        // Capture a single timestamp so createTime and updateTime are identical.
        LocalDateTime now = LocalDateTime.now();
        Student student = Student.builder()
                .name(studentSaveDTO.getName())
                .no(studentSaveDTO.getNo())
                .gender(studentSaveDTO.getGender())
                .phone(studentSaveDTO.getPhone())
                .idCard(studentSaveDTO.getIdCard())
                .address(studentSaveDTO.getAddress())
                .degree(studentSaveDTO.getDegree())
                .isCollege(studentSaveDTO.getIsCollege())
                .graduationDate(studentSaveDTO.getGraduationDate())
                .clazzId(studentSaveDTO.getClazzId())
                .createTime(now)
                .updateTime(now)
                .build();
        studentMapper.insert(student);
        // After insert, student.id is populated by MyBatis (useGeneratedKeys).
        // We re-fetch via getStudentById so the returned VO includes joined
        // fields (e.g., clazz_name from the LEFT JOIN in list query - not
        // currently fetched by this getter, but safe path for future enrichment).
        return studentMapper.getStudentById(student.getId());
    }

    /**
     * {@inheritDoc}
     *
     * <p>Implemented as a single scoped query ({@code getStudentByIdScoped},
     * which LEFT JOINs clazz) rather than a fetch-then-compare. When
     * {@code scopeMasterId} is non-null, an out-of-scope student simply doesn't
     * match the query and returns null, so it reuses the exact same null-to-404
     * path as a genuinely missing id — the two cases are indistinguishable to the
     * caller by design.
     */
    @Override
    public StudentVO getStudentById(Integer id, Integer scopeMasterId) {
        StudentVO studentVO = studentMapper.getStudentByIdScoped(id, scopeMasterId);
        if (studentVO == null) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
        return studentVO;
    }

    /**
     * {@inheritDoc}
     *
     * <p>Ownership is enforced in SQL: the UPDATE only matches when the row is
     * within scope, so an out-of-scope (or missing) row yields affected == 0 and
     * maps to 404 - same path, indistinguishable to the caller.
     */
    @Override
    @Transactional
    public StudentVO modifyStudentInfo(Integer id, StudentUpdateDTO studentUpdateDTO, Integer scopeMasterId) {
        Student student = Student.builder()
                .id(id)
                .name(studentUpdateDTO.getName())
                .no(studentUpdateDTO.getNo())
                .gender(studentUpdateDTO.getGender())
                .phone(studentUpdateDTO.getPhone())
                .idCard(studentUpdateDTO.getIdCard())
                .address(studentUpdateDTO.getAddress())
                .degree(studentUpdateDTO.getDegree())
                .isCollege(studentUpdateDTO.getIsCollege())
                .graduationDate(studentUpdateDTO.getGraduationDate())
                .clazzId(studentUpdateDTO.getClazzId())
                .updateTime(LocalDateTime.now())
                .build();
        int affected = studentMapper.update(student, scopeMasterId);
        if (affected == 0) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
        return studentMapper.getStudentById(id);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Ownership is enforced in SQL: the DELETE only matches when the row is
     * within scope, so an out-of-scope (or missing) row yields affected == 0 and
     * maps to 404 - same path, indistinguishable to the caller.
     */
    @Override
    @Transactional
    public void delete(Integer id, Integer scopeMasterId) {
        int affected = studentMapper.deleteById(id, scopeMasterId);
        if (affected == 0) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
    }

    /**
     * {@inheritDoc}
     *
     * <p>Ownership is enforced in SQL: the additive UPDATE (score += n, count += 1)
     * only matches when the row is within scope, so an out-of-scope (or missing) row
     * yields affected == 0 and maps to 404 - same path, indistinguishable to the
     * caller.
     */
    @Override
    @Transactional
    public StudentVO recordViolation(Integer studentId, Integer score, Integer scopeMasterId) {
        int affected = studentMapper.modifyViolationScore(studentId, score, scopeMasterId);
        if (affected == 0) {
            throw new ResourceNotFoundException("Student with id " + studentId + " not found");
        }
        return studentMapper.getStudentById(studentId);
    }
}
