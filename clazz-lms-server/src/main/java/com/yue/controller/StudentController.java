package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.PageResult;
import com.yue.pojo.Result;
import com.yue.pojo.dto.StudentSaveDTO;
import com.yue.pojo.dto.StudentUpdateDTO;
import com.yue.pojo.StudentQueryParam;
import com.yue.pojo.vo.StudentVO;
import com.yue.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * StudentController class
 */
@Slf4j
@RequestMapping("/students")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * query student list based on query params
     * @param studentQueryParam query params
     * @return
     */
    @GetMapping
    public Result page(StudentQueryParam studentQueryParam) {
        log.info("Student list query：{}", studentQueryParam);
        PageResult<StudentVO> pageResult = studentService.page(studentQueryParam);
        return Result.success(pageResult);
    }

    /**
     * add new student
     * @param studentSaveDTO student save dto object
     * @return add result
     */
    @Log
    @PostMapping
    public Result save(@RequestBody StudentSaveDTO studentSaveDTO) {
        log.info("Add new student：{}", studentSaveDTO);
        studentService.add(studentSaveDTO);
        return Result.success();
    }

    /**
     * query student by id
     * @param id student id
     * @return student vo
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        log.info("查询学员：{}", id);
        StudentVO studentVO = studentService.getStudentById(id);
        return Result.success(studentVO);
    }

    /**
     * update student info
     * @param studentUpdateDTO student update dto object
     * @return update result object
     */
    @Log
    @PutMapping
    public Result modifyStudentInfo(@RequestBody StudentUpdateDTO studentUpdateDTO) {
        log.info("Update student info：{}", studentUpdateDTO);
        studentService.modifyStudentInfo(studentUpdateDTO);
        return Result.success();
    }

    /**
     * delete student by id
     * @param id student id
     * @return delete result object
     */
    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除学员：{}", id);
        studentService.delete(id);
        return Result.success();
    }

    /**
     * violation violation score
     * @param id student id
     * @param score violation score object
     * @return violation result object
     */
    @Log
    @PutMapping("/violation/{id}/{score}")
    public Result modifyViolationScore(@PathVariable Integer id, @PathVariable Integer score) {
        log.info("Violation operation: {}, {}", id, score);
        studentService.modifyViolationScore(id, score);
        return Result.success();
    }
}
