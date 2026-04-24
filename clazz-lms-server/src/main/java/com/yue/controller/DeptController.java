package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.dto.DeptSaveDTO;
import com.yue.pojo.dto.DeptUpdateDTO;
import com.yue.pojo.vo.DeptVO;
import com.yue.service.DeptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Department REST controller.
 * <p>
 *     Follows REST conventions: returns DTOs directly, uses HTTP status codes for errors
 *     (delegated to GlobalExceptionHandler), and leverages proper HTTP semantics
 *     (201 Created for POST, 204 No Content for DELETE).
 * </p>
 * Dept: department
 */
@Slf4j // log annotation
@RestController // rest controller annotation
@RequestMapping("/depts") // request mapping annotation: /depts
@RequiredArgsConstructor
public class DeptController {

    private final DeptService deptService;

    /**
     * List all departments
     *
     * @return 200 OK with the list of departments (possibly empty)
     */
    @GetMapping
    public List<DeptVO> list() {
        log.info("Query all departments");
        return deptService.findAll();
    }

    /**
     * Get a department by id.
     *
     * @param id department ID
     * @return 200 OK with department info; 404 if not found
     */
    @GetMapping("/{id}")
    public DeptVO getInfo(@PathVariable Integer id) {
        log.info("Query department info by id：{}", id);
        return deptService.getById(id);
    }

    /**
     * Create a new department.
     *
     * @param deptSaveDTO department creation payload
     * @return 201 Created with Location header pointing to the new resource
     */
    @Log
    @PostMapping
    public ResponseEntity<DeptVO> add(@RequestBody DeptSaveDTO deptSaveDTO) {
        log.info("Add new department：{}", deptSaveDTO);
        DeptVO created  = deptService.add(deptSaveDTO);

        // Build the URI for the newly created resource: /depts/{id}
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return  ResponseEntity.created(location).body(created);
    }

    /**
     * Update an existing department.
     *
     * @param id department ID (from URL, authoritative)
     * @param deptUpdateDTO department upload payload
     * @return 200 OK with the updated department; 404 if not found
     */
    @Log
    @PutMapping("/{id}")
    public DeptVO update(
            @PathVariable Integer id,
            @RequestBody DeptUpdateDTO deptUpdateDTO) {
        log.info("Update department id={}, payload={}", id, deptUpdateDTO);
        return deptService.update(id, deptUpdateDTO);
    }

    /**
     * Delete a department by id.
     *
     * @param id department id
     * <p>
     *           204 No Content on success
     *           404 if not found
     *           409 if the department still has employees
     */
    @Log
    @DeleteMapping("/{id}")
    public void deleteDeptById(@PathVariable Integer id) {
        log.info("Delete department by id：{}", id);
        deptService.deleteById(id);
    }
}
