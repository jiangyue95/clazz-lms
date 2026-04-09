package com.jiangyue.controller;

import com.jiangyue.pojo.Clazz;
import com.jiangyue.pojo.ClazzQueryParam;
import com.jiangyue.pojo.PageResult;
import com.jiangyue.pojo.Result;
import com.jiangyue.service.ClazzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/clazzs")
@RestController
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    /**
     * 分页查询班级列表
     * @param clazzQueryParam
     * @return
     */
    @GetMapping
    public Result page(ClazzQueryParam clazzQueryParam) {
        log.info("班级分页查询： {}", clazzQueryParam);
        PageResult<Clazz> pageResult = clazzService.page(clazzQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 新增班级
     * @param clazz 前端传入的班级信息 JSON 格式
     * @return
     */
    @PostMapping
    public Result save(@RequestBody Clazz clazz) {
        log.info("新增班级：{}", clazz);
        clazzService.save(clazz);
        return Result.success();
    }

    /**
     * 根据路径中的 Id 查询班级
     * @param id 路径中的 id
     * @return 返回 JSON 格式的班级信息
     */
    @GetMapping("/{id}")
    public Result getClazzById(@PathVariable Integer id) {
        log.info("查询班级：{}", id);
        Clazz clazz =  clazzService.getClassById(id);
        return Result.success(clazz);
    }

    /**
     * 根据前端传入 JSON 格式班级信息进行修改
     * @param clazz 请求体中的班级信息
     * @return
     */
    @PutMapping
    public Result modifyClazz(@RequestBody Clazz clazz) {
        log.info("修改班级：{}", clazz);
        clazzService.modifyClazz(clazz);
        return Result.success();
    }

    /**
     * 根据路径中的 id 删除班级
     * @param id 路径中的 id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result removeClazzById(@PathVariable Integer id){
        log.info("删除班级：{}", id);
        clazzService.deleteClazzById(id);
        return Result.success();
    }

    @GetMapping("/list")
    public Result getAllClazzs() {
        log.info("查询所有班级");
        List<Clazz> clazzList = clazzService.getAllClazzs();
        return Result.success(clazzList);
    }
}
