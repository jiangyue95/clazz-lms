package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.PageResult;
import com.yue.pojo.Result;
import com.yue.pojo.Student;
import com.yue.pojo.StudentQueryParam;
import com.yue.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/students")
@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 根据查询参数获取 Student 对象列表
     * @param studentQueryParam 封装了查询参数的对象
     * @return 返回 Result 对象
     */
    @GetMapping
    public Result page(StudentQueryParam studentQueryParam) {
        log.info("学生列表查询：{}", studentQueryParam);
        PageResult<Student> pageResult = studentService.page(studentQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 根据请求体中的信息新增学员
     * @param student 请求体中的学员信息
     * @return 返回 Result 对象
     */
    @Log
    @PostMapping
    public Result save(@RequestBody Student student) {
        log.info("增加学员：{}", student);
        studentService.add(student);
        return Result.success();
    }

    /**
     * 根据 id 获取学生对象
     * @param id 路径中的 id
     * @return 返回 Result 对象
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        log.info("查询学员：{}", id);
        Student student = studentService.getStudentById(id);
        return Result.success(student);
    }

    /**
     * 修改学员信息
     * @param student 请求体中的学员信息
     * @return 返回 Result 对象
     */
    @Log
    @PutMapping
    public Result modifyStudentInfo(@RequestBody Student student) {
        log.info("修改学员信息：{}", student);
        studentService.modifyStudentInfo(student);
        return Result.success();
    }

    /**
     * 根据 id 删除学员
     * @param id 删除的 id
     * @return 删除结果
     */
    @Log
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        log.info("删除学员：{}", id);
        studentService.delete(id);
        return Result.success();
    }

    /**
     * 违纪处理
     * @param id 违纪的学员 id
     * @param score 扣的分数
     * @return 扣分结果
     */
    @Log
    @PutMapping("/violation/{id}/{score}")
    public Result modifyViolationScore(@PathVariable Integer id, @PathVariable Integer score) {
        log.info("违纪处理: {}, {}", id, score);
        studentService.modifyViolationScore(id, score);
        return Result.success();
    }
}
