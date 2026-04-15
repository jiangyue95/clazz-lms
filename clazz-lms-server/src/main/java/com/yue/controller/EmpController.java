package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.dto.EmpListQueryDTO;
import com.yue.pojo.entity.Emp;
import com.yue.pojo.EmpQueryParam;
import com.yue.pojo.PageResult;
import com.yue.pojo.Result;
import com.yue.pojo.vo.EmpVO;
import com.yue.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

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

    /**
     * Based on the query parameters, return a list of employee.
     * @param empListQueryDTO query parameters
     * @return a list of employee
     */
    @GetMapping
    public Result page(EmpListQueryDTO empListQueryDTO){
        log.info("Pagination Search：{}", empListQueryDTO);
        PageResult<EmpVO> pageResult = empService.page(empListQueryDTO);
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
     * Delete employee by id
     * @param ids a list of employee id
     * @return Result object
     */
    @Log
    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids) {
        log.info("Delete parameter：{}", ids);
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
