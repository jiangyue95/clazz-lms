package com.yue.mapper;

import com.yue.pojo.Student;
import com.yue.pojo.StudentQueryParam;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    /**
     * 根据学生查询参数对象查询学生
     * 
     * @param studentQueryParam 学生查询参数对象
     * @return 包含符合要求的 Student 对象 List
     */
    public List<Student> list(StudentQueryParam studentQueryParam);

    /**
     * 根据 id 查询班级人数
     * 
     * @param id 班级 id
     * @return 班级人数
     */
    @Select("SELECT COUNT(*) FROM student WHERE clazz_id = #{id}")
    Integer countByClazzId(Integer id);

    /**
     * 插入新学员
     * 
     * @param student
     */
    void insert(Student student);

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
    Student getStudentById(Integer id);

    /**
     * 根据 id 修改学生
     * @param student 修改的 Student 对象
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

    @MapKey("name")
    List<Map<String, Object>> countStudentDegreeData();
}
