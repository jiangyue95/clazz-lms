package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.dto.ClazzSaveDTO;
import com.yue.pojo.dto.ClazzUpdateDTO;
import com.yue.pojo.ClazzQueryParam;
import com.yue.pojo.PageResult;
import com.yue.pojo.Result;
import com.yue.pojo.vo.ClazzVO;
import com.yue.service.ClazzService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clazz(class) controller
 */
@Slf4j
@RequestMapping("/clazzs")
@RestController
@RequiredArgsConstructor
public class ClazzController {

    private final ClazzService clazzService;

    /**
     * Pagination query clazz list
     * @param clazzQueryParam query parameters
     * @return result
     */
    @GetMapping
    public Result<PageResult<ClazzVO>> page(ClazzQueryParam clazzQueryParam) {
        log.info("Query clazz(class) list by pagination: {}", clazzQueryParam);
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
    public Result<Void> save(@RequestBody ClazzSaveDTO clazzSaveDTO) {
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
    public Result<ClazzVO> getClazzById(@PathVariable Integer id) {
        log.info("Query clazz(class) by id：{}", id);
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
    public Result<Void> modifyClazz(@RequestBody ClazzUpdateDTO clazzUpdateDTO) {
        log.info("Update clazz(class) info：{}", clazzUpdateDTO);
        clazzService.modifyClazz(clazzUpdateDTO.getId(), clazzUpdateDTO);
        return Result.success();
    }

    /**
     * Delete clazz by id
     * @param id clazz id
     * @return result
     */
    @Log
    @DeleteMapping("/{id}")
    public Result<Void> removeClazzById(@PathVariable Integer id){
        log.info("Delete clazz(class) by id：{}", id);
        clazzService.deleteClazzById(id);
        return Result.success();
    }

    /**
     * Query all clazz(class) list
     * @return clazz(class) vo list
     */
    @GetMapping("/list")
    public Result<List<ClazzVO>> getAllClazzs() {
        log.info("Query all clazz(class) list");
        List<ClazzVO> clazzList = clazzService.getAllClazzs();
        return Result.success(clazzList);
    }
}
