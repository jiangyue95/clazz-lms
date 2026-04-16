package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.dto.DeptSaveDTO;
import com.yue.pojo.dto.DeptUpdateDTO;
import com.yue.pojo.entity.Dept;
import com.yue.pojo.Result;
import com.yue.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Department controller
 * Dept: department
 */
@Slf4j // log annotation
@RequestMapping("/depts") // request mapping annotation: /depts
@RestController // rest controller annotation
public class DeptController {

    @Autowired
    private DeptService deptService;

    /**
     * query all department info
     * @return all department info
     */
    @GetMapping
    public Result list() {
        log.info("Query all department info");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

    /**
     * add new department
     * @param deptSaveDTO new department info
     * @return add success info
     */
    @Log
    @PostMapping
    public Result add(@RequestBody DeptSaveDTO deptSaveDTO) {
        log.info("Add new department：{}", deptSaveDTO);
        deptService.add(deptSaveDTO);
        return Result.success();
    }

    /**
     * query department info by id
     * @param id department ID
     * @return department info
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("Query department info by id：{}", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    /**
     * update department info
     * @param deptUpdateDTO department info
     * @return update success info
     */
    @Log
    @PutMapping
    public Result update(@RequestBody DeptUpdateDTO deptUpdateDTO) {
        log.info("Update department：{}", deptUpdateDTO);
        deptService.update(deptUpdateDTO);
        return Result.success();
    }

    /**
     * delete department by id
     * @param id department ID
     * @return delete success info
     */
    @Log
    @DeleteMapping("/{id}")
    public Result deleteDeptById(@PathVariable Integer id) {
        log.info("Delete department by id：{}", id);
        deptService.deleteById(id);
        return Result.success();
    }
}
