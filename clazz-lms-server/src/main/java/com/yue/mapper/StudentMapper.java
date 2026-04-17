package com.yue.mapper;

import com.yue.pojo.entity.Student;
import com.yue.pojo.StudentQueryParam;
import com.yue.pojo.vo.StudentDegreeVO;
import com.yue.pojo.vo.StudentVO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * StudentMapper interface
 */
@Mapper
public interface StudentMapper {

    /**
     * query student list based on query params
     * @param studentQueryParam query params
     * @return student list
     */
    List<StudentVO> list(StudentQueryParam studentQueryParam);

    /**
     * query student count
     * @param id clazz(class) id
     * @return
     */
    @Select("SELECT COUNT(*) FROM student WHERE clazz_id = #{id}")
    Integer countByClazzId(Integer id);

    /**
     * insert new student
     * @param student student entity
     */
    void insert(Student student);

    /**
     * query student by id
     * @param id student id
     * @return student vo
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
     * update student info
     * @param student student entity
     */
    void update(Student student);

    /**
     * 根据 id 删除学生
     * @param id 删除学生的 id
     */
    @Delete("DELETE FROM student WHERE id = #{id}")
    void deleteById(Integer id);

    void modifyViolationScore(Integer id, Integer score);

    /**
     * 获取班级人数
     * @return 班级人数
     */
    @MapKey("clazz_name")
    List<Map<String, Object>> countStudentClazzData();

    List<StudentDegreeVO> countStudentDegreeData();
}
