package com.yue.service;

import com.yue.pojo.PageResult;
import com.yue.pojo.dto.StudentSaveDTO;
import com.yue.pojo.dto.StudentUpdateDTO;
import com.yue.pojo.StudentQueryParam;
import com.yue.pojo.vo.StudentVO;

/**
 * StudentService interface
 */
public interface StudentService {

    /**
     * Get student list based on query params
     * @param studentQueryParam query params
     * @return PageResult<StudentVO>
     */
    PageResult<StudentVO> page(StudentQueryParam studentQueryParam);

    /**
     * Add new student
     * @param studentSaveDTO a StudentSaveDTO object
     */
    void add(StudentSaveDTO studentSaveDTO);

    /**
     * Get student by id
     * @param id student id
     * @return StudentVO object
     */
    StudentVO getStudentById(Integer id);

    /**
     * modify student info
     * @param studentUpdateDTO student info dto
     */
    void modifyStudentInfo(StudentUpdateDTO studentUpdateDTO);

    /**
     * delete student by id
     * @param id student id to delete
     */
    void delete(Integer id);

    /**
     * modify student violation score
     * @param id student id
     * @param score violation score to modify
     */
    void modifyViolationScore(Integer id, Integer score);
}
