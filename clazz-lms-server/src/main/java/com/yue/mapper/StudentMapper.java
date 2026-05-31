package com.yue.mapper;

import com.yue.pojo.entity.Student;
import com.yue.pojo.dto.StudentQueryParam;
import com.yue.pojo.vo.StudentVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * StudentMapper interface.
 *
 * <p>Write methods ({@code insert}, {@code update}, {@code deleteById},
 * {@code modifyViolationScore}) return {@code int} - the number of rows
 * affected. The service layer uses this to detect missing rows (affected = 0)
 * and translate them into {@link com.yue.exception.ResourceNotFoundException}
 * for a clean 404 response.
 */
@Mapper
public interface StudentMapper {

    /**
     * Query student list based on query params.
     *
     * @param studentQueryParam query params
     * @return student list (possibly empty, never null)
     */
    List<StudentVO> list(StudentQueryParam studentQueryParam);

    /**
     * Query student count for a given clazz.
     *
     * @param id clazz(class) id
     * @return count of students in that clazz
     */
    @Select("SELECT COUNT(*) FROM student WHERE clazz_id = #{id}")
    Integer countByClazzId(Integer id);

    /**
     * Insert a new student. The generated primary key is written back into
     * the {@code id} field of the {@link Student} entity via MyBatis
     * {@code useGeneratedKeys}.
     *
     * @param student student entity (will have {@code id} populated on return)
     * @return 1 if inserted, otherwise 0
     */
    int insert(Student student);

    /**
     * Query student by id.
     *
     * @param id student id
     * @return student VO, or {@code null} if no student with that id exists
     */
    @Select("SELECT " +
            "id, " +
            "name, " +
            "no, " +
            "phone, " +
            "gender, " +
            "degree, " +
            "id_card, " +
            "is_college, " +
            "address, " +
            "graduation_date, " +
            "violation_count, " +
            "violation_score, " +
            "clazz_id, " +
            "create_time, " +
            "update_time " +
            "FROM student " +
            "WHERE id = #{id}")
    StudentVO getStudentById(Integer id);

    /**
     * Update student info.
     *
     * @param student student entity (id required, other fields optional)
     * @return number of rows affected (1 if updated, 0 if no student with that id)
     */
    int update(Student student);

    /**
     * Delete a student by id.
     *
     * @param id student id
     * @return number of rows affected (1 if deleted, 0 if no student with that id)
     */
    @Delete("DELETE FROM student WHERE id = #{id}")
    int deleteById(Integer id);

    /**
     * Record a violation: increment {@code violation_score} by the given amount
     * and increment {@code violation_count} by 1, automatically in a single SQL.
     *
     * @param id student id
     * @param score the violation score to add (additive, not replacement)
     * @return number of rows affected (1 if student exists, 0 otherwise)
     */
    int modifyViolationScore(Integer id, Integer score);

    /**
     * Get clazz(class) student count data.
     *
     * @return a map of clazz(class) name and count
     */
    @MapKey("clazz_name")
    List<Map<String, Object>> countStudentClazzData();

    /**
     * Get student degree data.
     *
     * @return a map of degree and count
     */
    @MapKey("name")
    List<Map<String, Object>> countStudentDegreeData();
}
