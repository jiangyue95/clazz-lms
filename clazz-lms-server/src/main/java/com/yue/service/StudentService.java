package com.yue.service;

import com.yue.pojo.PageResult;
import com.yue.pojo.entity.Student;
import com.yue.pojo.StudentQueryParam;

public interface StudentService {

    /**
     * 根据查询参数获取 Student 列表
     * @param studentQueryParam 查询参数对象
     * @return PageResult 对象
     */
    PageResult<Student> page(StudentQueryParam studentQueryParam);

    /**
     * 增加 student
     * @param student Student 对象
     */
    void add(Student student);

    /**
     * 根据 id 查询 学生对象
     * @param id 传入的 id
     * @return 返回学生对象
     */
    Student getStudentById(Integer id);

    /**
     * 根据传入的 student 对象传入 Mapper 层
     * @param student student 对象
     */
    void modifyStudentInfo(Student student);

    /**
     * 根据传入的 id 删除 student
     * @param id 删除的 id
     */
    void delete(Integer id);

    /**
     * 根据传入的 id 修改 student 的违规分
     * @param id 删除的 id
     * @param score 违规分
     */
    void modifyViolationScore(Integer id, Integer score);
}
