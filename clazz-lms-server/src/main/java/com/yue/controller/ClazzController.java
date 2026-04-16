package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.dto.ClazzSaveDTO;
import com.yue.pojo.dto.ClazzUpdateDTO;
import com.yue.pojo.ClazzQueryParam;
import com.yue.pojo.PageResult;
import com.yue.pojo.Result;
import com.yue.pojo.vo.ClazzVO;
import com.yue.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clazz(class) controller
 */
@Slf4j
@RequestMapping("/clazzs")
@RestController
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * Pagination query clazz list
     * @param clazzQueryParam query parameters
     * @return result
     */
    @GetMapping
    public Result page(ClazzQueryParam clazzQueryParam) {
        log.info("班级分页查询： {}", clazzQueryParam);
        PageResult<ClazzVO> pageResult = clazzService.page(clazzQueryParam);
        return Result.success(pageResult);
    }

    /**
     * Add new clazz(class)
     * @param clazzSaveDTO clazz save dto
     * @return result
     */
    @Log
    @PostMapping
    public Result save(@RequestBody ClazzSaveDTO clazzSaveDTO) {
        log.info("Add new clazz(class)：{}", clazzSaveDTO);
        clazzService.save(clazzSaveDTO);
        return Result.success();
    }

    /**
     * Query clazz by id
     * @param id clazz id
     * @return clazz vo
     */
    @GetMapping("/{id}")
    public Result getClazzById(@PathVariable Integer id) {
        log.info("查询班级：{}", id);
        ClazzVO clazzVO =  clazzService.getClassById(id);
        return Result.success(clazzVO);
    }

    /**
     * Update clazz by id
     * @param clazzUpdateDTO clazz update dto
     * @return result
     */
    @Log
    @PutMapping()
    public Result modifyClazz(@RequestBody ClazzUpdateDTO clazzUpdateDTO) {
        log.info("Update clazz(class) info：{}", clazzUpdateDTO);
        clazzService.modifyClazz(clazzUpdateDTO);
        return Result.success();
    }

    /**
     * Delete clazz by id
     * @param id clazz id
     * @return result
     */
    @Log
    @DeleteMapping("/{id}")
    public Result removeClazzById(@PathVariable Integer id){
        log.info("Delete clazz(class)：{}", id);
        clazzService.deleteClazzById(id);
        return Result.success();
    }

    /**
     * Query all clazz(class) list
     * @return clazz(class) vo list
     */
    @GetMapping("/list")
    public Result getAllClazzs() {
        log.info("Query all clazz(class)");
        List<ClazzVO> clazzList = clazzService.getAllClazzs();
        return Result.success(clazzList);
    }
}
