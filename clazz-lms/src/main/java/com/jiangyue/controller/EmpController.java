package com.jiangyue.controller;

import com.jiangyue.anno.Log;
import com.jiangyue.pojo.Emp;
import com.jiangyue.pojo.EmpQueryParam;
import com.jiangyue.pojo.PageResult;
import com.jiangyue.pojo.Result;
import com.jiangyue.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * 员工管理的 Controller
 */
@Slf4j
@RestController
@RequestMapping("/emps")
public class EmpController {

    @Autowired
    private EmpService empService;
    @Autowired
    private ResourceUrlProvider resourceUrlProvider;

//    /**
//     * 分页查询
//     * @return
//     */
//    @GetMapping
//    public Result page(
//            @RequestParam(defaultValue = "") String name,
//            @RequestParam(defaultValue = "") Integer gender,
//            @RequestParam(defaultValue = "1970-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
//            @RequestParam(defaultValue = "2100-01-01") @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end,
//            @RequestParam(defaultValue = "1") Integer page,
//            @RequestParam(defaultValue = "10") Integer pageSize){
//        log.info("分页查询：{}, {}, {}, {}, {}, {}", name, gender, begin, end, page, pageSize);
//        PageResult<Emp> pageResult = empService.list(name, gender, begin, end, page, pageSize);
//        return Result.success(pageResult);
//    }

    /**
     * 根据条件查询列表
     * @param empQueryParam
     * @return
     */
    @GetMapping
    public Result page(EmpQueryParam empQueryParam){
        log.info("分页查询：{}", empQueryParam);
        PageResult<Emp> pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    /**
     * 新增员工
     * @param emp 一个 Emp 员工对象
     * @return Result对象
     */
    @Log
    @PostMapping
    public Result save(@RequestBody Emp emp) throws Exception {
        log.info("新增员工：{}", emp);
        empService.save(emp);
        return Result.success();
    }

    /**
     * 删除员工
     * @return
     */
//    @DeleteMapping
//    public Result delete(Integer[] ids) {
//        log.info("删除员工信息：{}", Arrays.toString(ids));
//        return Result.success();
//
//    }

    @Log
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("删除参数：{}", ids);
        empService.delete(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("根据 ID 查询员工信息：{}", id);
        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    @Log
    @PutMapping
    public Result update(@RequestBody Emp emp) {
        log.info("修改员工：{}", emp);
        empService.update(emp);
        return Result.success();
    }

    /**
     * 查询全部员工
     * @return 包含全部员工数据的 Result 对象
     */
    @GetMapping("/list")
    public Result getAllEmp() {
        log.info("查询全部员工");
        List<Emp> allEmps = empService.getAllEmp();
        return Result.success(allEmps);
    }
}
