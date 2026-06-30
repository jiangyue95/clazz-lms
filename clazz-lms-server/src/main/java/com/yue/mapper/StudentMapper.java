package com.yue.mapper;

import com.yue.pojo.dto.StudentQueryParam;
import com.yue.pojo.entity.Student;
import com.yue.pojo.vo.StudentVO;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
     * @param student the student entity carrying id (required) and the
     *                filed to update
     * @param scopeMasterId when non-null, the update only matches a row whose class
     *                      master_id equals this value; when null, no ownership
     *                      filter is applied
     * @return number of rows affected (1 if updated, 0 if no student with that id)
     */
    int update(@Param("student") Student student, @Param("scopeMasterId") Integer scopeMasterId);

    /**
     * Delete a student by id.
     *
     * @param id student id
     * @param scopeMasterId when non-null, the row is deleted only if its class
     *                      master_id equals this value; when null, no ownership filter is applied
     * @return number of rows affected (1 if deleted, 0 if no student with that id)
     */
    int deleteById(@Param("id") Integer id, @Param("scopeMasterId") Integer scopeMasterId);

    /**
     * Record a violation: increment {@code violation_score} by the given amount
     * and increment {@code violation_count} by 1, automatically in a single SQL.
     *
     * @param id student id
     * @param score the violation score to add (additive, not replacement)
     * @param scopeMasterId when non-null, the update only matches a row whose class
     *                      master_id equals this value; when null, no ownership filter is applied
     * @return number of rows affected (1 if student exists, 0 otherwise)
     */
    int modifyViolationScore(@Param("id") Integer id,
                             @Param("score") Integer score,
                             @Param("scopeMasterId") Integer scopeMasterId);

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

    /**
     * Query a single student by id, optionally by class ownership.
     *
     * @param id student id
     * @param scopeMasterId when non-null, the query additionally requires the
     *                      student's class master_id to equal this value; when
     *                      null, no ownership filter is applied
     * @return the matching student VO, or {@code null} if no student matched
     *         (no such id, or out of the given scope)
     */
    StudentVO getStudentByIdScoped(@Param("id") Integer id, @Param("scopeMasterId") Integer scopeMasterId);
}
