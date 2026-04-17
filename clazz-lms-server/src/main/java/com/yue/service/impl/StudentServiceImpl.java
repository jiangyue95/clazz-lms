package com.yue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yue.mapper.StudentMapper;
import com.yue.pojo.PageResult;
import com.yue.pojo.dto.StudentSaveDTO;
import com.yue.pojo.dto.StudentUpdateDTO;
import com.yue.pojo.entity.Student;
import com.yue.pojo.StudentQueryParam;
import com.yue.pojo.vo.StudentVO;
import com.yue.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    /**
     * Get student list based on query params
     * @param studentQueryParam query params
     * @return PageResult<StudentVO>
     */
    @Override
    public PageResult<StudentVO> page(StudentQueryParam studentQueryParam) {
        // 1. set pagination params using PageHelper
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        // 2. execute query
        List<StudentVO> studentList = studentMapper.list(studentQueryParam);
        // 3. parse query result and wrap it in PageResult
        Page<StudentVO> p = (Page<StudentVO>) studentList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    /**
     * Add new student
     * @param studentSaveDTO a StudentSaveDTO object
     */
    @Override
    public void add(StudentSaveDTO studentSaveDTO) {
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
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        studentMapper.insert(student);
    }

    /**
     * 将传入的 id 传给 mapper 层
     * @param id 传入的 id
     * @return StudentVO 对象
     */
    @Override
    public StudentVO getStudentById(Integer id) {
        StudentVO studentVO = studentMapper.getStudentById(id);
        return studentVO;
    }

    /**
     * modify student info
     * @param studentUpdateDTO student info dto
     */
    @Override
    public void modifyStudentInfo(StudentUpdateDTO studentUpdateDTO) {
        Student student = Student.builder()
                .id(studentUpdateDTO.getId())
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
