package com.yue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yue.exception.ResourceNotFoundException;
import com.yue.mapper.StudentMapper;
import com.yue.pojo.PageResult;
import com.yue.pojo.dto.StudentQueryParam;
import com.yue.pojo.dto.StudentSaveDTO;
import com.yue.pojo.dto.StudentUpdateDTO;
import com.yue.pojo.entity.Student;
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
 * <p>Existence is enforced at the service layer; any method operating on a
 * specific student verifies the student exists (via affected-rows check on
 * writes, or null-check on reads) and throws {@link ResourceNotFoundException}
 * if not. This keeps 404 handling out of every controller and in a single
 * layer, mapped centrally by {@code GlobalExceptionHandler}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentMapper studentMapper;

    /**
     * Page-query the student list with optional filters.
     *
     * @param studentQueryParam filter and pagination parameters
     * @return a paged result of students (possibly empty, never {@code null})
     */
    @Override
    public PageResult<StudentVO> page(StudentQueryParam studentQueryParam) {
        // PageHelper intercepts the very next MyBatis query on this thread
        // and applies LIMIT/OFFSET, then exposes total count via the Page wrapper.
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        List<StudentVO> studentList = studentMapper.list(studentQueryParam);
        Page<StudentVO> p = (Page<StudentVO>) studentList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    /**
     * Create a new student.
     *
     * <p> The generated primary key is back-filled into the entity by MyBatis
     * ({@code useGenerateKeys}), after which the full VO is re-fetched so the
     * caller receives exactly what was persisted (including any DB-defaulted
     * fields).
     *
     * @param studentSaveDTO student creation payload
     * @return the newly created student
     */
    @Override
    @Transactional
    public StudentVO add(StudentSaveDTO studentSaveDTO) {
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
     * Look up a single student by id.
     *
     * @param id student id
     * @return the matching student
     * @throws ResourceNotFoundException if no student with the given id exists
     */
    @Override
    public StudentVO getStudentById(Integer id) {
        StudentVO studentVO = studentMapper.getStudentById(id);
        if (studentVO == null) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
        return studentVO;
    }

    /**
     * Update an existing student's information.
     *
     * <p>The {@code id} argument is authoritative; any id carried inside the
     * DTO is ignored. THe mapper's affected-row count is checked to detect a
     * non-existent target.
     *
     * @param id student id (from the URL path)
     * @param studentUpdateDTO update payload
     * @throws ResourceNotFoundException if no student with the given id exists
     */
    @Override
    @Transactional
    public StudentVO modifyStudentInfo(Integer id, StudentUpdateDTO studentUpdateDTO) {
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
        int affected = studentMapper.update(student);
        if (affected == 0) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
        return studentMapper.getStudentById(id);
    }

    /**
     * Delete a student by id.
     *
     * <p>Strict semantics: deleting a non-existing student is treated as an
     * error(404), not a silent no-op.
     *
     * @param id student id
     * @throws ResourceNotFoundException if no student with the given id exists
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        int affected = studentMapper.deleteById(id);
        if (affected == 0) {
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
    }

    /**
     * Record a violation against a student
     *
     * <p>Atomically increments {@code violation_score} by {@code socore} and
     * {@code violation_count} by one in a single SQL statement. Additive, not
     * a replacement.
     *
     * @param studentId student id
     * @param score violation points to add
     * @return the updated student, reflecting the new score and count
     * @throws ResourceNotFoundException if no student with the given id exists
     */
    @Override
    @Transactional
    public StudentVO recordViolation(Integer studentId, Integer score) {
        int affected = studentMapper.modifyViolationScore(studentId, score);
        if (affected == 0) {
            throw new ResourceNotFoundException("Student with id " + studentId + " not found");
        }
        return studentMapper.getStudentById(studentId);
    }
}
