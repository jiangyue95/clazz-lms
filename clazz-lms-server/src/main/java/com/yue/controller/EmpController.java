package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.dto.EmpListQueryDTO;
import com.yue.pojo.dto.EmpSaveDTO;
import com.yue.pojo.dto.EmpUpdateDTO;
import com.yue.pojo.entity.Emp;
import com.yue.pojo.EmpQueryParam;
import com.yue.pojo.PageResult;
import com.yue.pojo.Result;
import com.yue.pojo.vo.EmpInfoVO;
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
     * Add employee
     * @param empSaveDTO employee save dto
     * @return Result object
     * @throws Exception
     */
    @Log
    @PostMapping
    public Result save(@RequestBody EmpSaveDTO empSaveDTO) throws Exception {
        log.info("Add employee：{}", empSaveDTO);
        empService.save(empSaveDTO);
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

    /**
     * Get employee info by id
     * @param id employee id
     * @return employee info
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id) {
        log.info("Get employee info by id：{}", id);
        EmpInfoVO empInfoVO = empService.getInfo(id);
        return Result.success(empInfoVO);
    }

    /**
     * Update employee info
     * @param empUpdateDTO employee update dto
     * @return Result object
     */
    @Log
    @PutMapping
    public Result update(@RequestBody EmpUpdateDTO empUpdateDTO) {
        log.info("Update employee info：{}", empUpdateDTO);
        empService.update(empUpdateDTO);
        return Result.success();
    }

    /**
     * query all employee basic information
     * @return a list of employee basic information
     */
    @GetMapping("/list")
    public Result getAllEmp() {
        log.info("query all employee basic information");
        List<EmpVO> allEmps = empService.getAllEmp();
        return Result.success(allEmps);
    }
}
