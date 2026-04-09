package com.jiangyue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jiangyue.mapper.StudentMapper;
import com.jiangyue.pojo.Emp;
import com.jiangyue.pojo.PageResult;
import com.jiangyue.pojo.Student;
import com.jiangyue.pojo.StudentQueryParam;
import com.jiangyue.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * 将 StudentQueryParam 对象传入 Mapper 层获取 PageResult 对象
     * @param studentQueryParam 查询参数对象
     * @return PageResult 对象
     */
    @Override
    public PageResult<Student> page(StudentQueryParam studentQueryParam) {
        // 1. 设置分页参数（PageHelper)
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        // 2. 执行查询
        List<Student> studentList = studentMapper.list(studentQueryParam);
        // 3. 解析查询结果，并封装
        Page<Student> p = (Page<Student>) studentList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    /**
     * 将传入的 Student 对象保存到数据库
     * @param student Student 对象
     */
    @Override
    public void add(Student student) {
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }

    /**
     * 将传入的 id 传给 mapper 层
     * @param id 传入的 id
     * @return student 对象
     */
    @Override
    public Student getStudentById(Integer id) {
        Student student = studentMapper.getStudentById(id);
        return student;
    }

    /**
     * 根据传入的 student 对象，查询并修改已有的 student 对象
     * @param student student 对象
     */
    @Override
    public void modifyStudentInfo(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    /**
     * 根据传入的 id 删除已存在的 student
     * @param id 删除的 id
     */
    @Override
    public void delete(Integer id) {
        studentMapper.deleteById(id);
    }

    /**
     * 根据传入的 id 和 score 修改已存在的 student 的 violation_score
     * @param id 修改的 id
     * @param score 修改的 score
     */
    @Override
    public void modifyViolationScore(Integer id, Integer score) {
        studentMapper.modifyViolationScore(id, score);
    }
}
