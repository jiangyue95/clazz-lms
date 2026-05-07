package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.dto.EmpListQueryDTO;
import com.yue.pojo.dto.EmpSaveDTO;
import com.yue.pojo.dto.EmpUpdateDTO;
import com.yue.pojo.PageResult;
import com.yue.pojo.vo.EmpInfoVO;
import com.yue.pojo.vo.EmpVO;
import com.yue.service.EmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Employee management controller
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/emps")
public class EmpController {

    private final EmpService empService;

    /**
     * Pagination query of employee list.
     *
     * @param empListQueryDTO query parameters
     * @return 200 OK with the paged result (possibly empty)
     */
    @GetMapping
    public ResponseEntity<PageResult<EmpVO>> page(EmpListQueryDTO empListQueryDTO){
        log.info("Pagination Search：{}", empListQueryDTO);
        PageResult<EmpVO> pageResult = empService.page(empListQueryDTO);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * Add a new employee.
     *
     * @param empSaveDTO employee creation payload
     * @return 201 Created with the new resource and a Location header pointing to it
     */
    @Log
    @PostMapping
    public ResponseEntity<EmpInfoVO> save(@RequestBody EmpSaveDTO empSaveDTO) throws Exception {
        log.info("Add employee：{}", empSaveDTO);
        EmpInfoVO created = empService.save(empSaveDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Batch delete employees by ids.
     *
     * @param ids a list of employee ids
     * @return 204 No Content on success
     */
    @Log
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam List<Integer> ids) {
        log.info("Delete parameter：{}", ids);
        empService.delete(ids);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get employee info by id.
     *
     * @param id employee id
     * @return 200 OK with employee info; 404 if not found (handled centrally)
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpInfoVO> getInfo(@PathVariable Integer id) {
        log.info("Get employee info by id：{}", id);
        EmpInfoVO empInfoVO = empService.getInfo(id);
        return ResponseEntity.ok(empInfoVO);
    }

    /**
     * Update employee by id.
     *
     * @param id employee id (from URL, authoritative)
     * @param empUpdateDTO update payload
     * @return 200 OK with the updated employee; 404 if not found
     */
    @Log
    @PutMapping("/{id}")
    public ResponseEntity<EmpInfoVO> update(
            @PathVariable Integer id,
            @RequestBody EmpUpdateDTO empUpdateDTO) {
        log.info("Update employee id={}, payload={}", id, empUpdateDTO);
        EmpInfoVO updated = empService.update(id, empUpdateDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Query all employee basic information.
     *
     * @return 200 OK with all employees(possibly empty)
     */
    @GetMapping("/list")
    public ResponseEntity<List<EmpVO>> getAllEmp() {
        log.info("Query all employee basic information");
        List<EmpVO> allEmps = empService.getAllEmp();
        return ResponseEntity.ok(allEmps);
    }
}
