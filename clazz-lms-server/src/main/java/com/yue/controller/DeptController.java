package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.dto.DeptSaveDTO;
import com.yue.pojo.dto.DeptUpdateDTO;
import com.yue.pojo.vo.DeptVO;
import com.yue.service.DeptService;
import jakarta.validation.Valid;
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
    public ResponseEntity<List<DeptVO>> list() {
        log.info("Query all departments");
        List<DeptVO> depts = deptService.findAll();
        return ResponseEntity.ok(depts);
    }

    /**
     * Get a department by id.
     *
     * @param id department ID
     * @return 200 OK with department info; 404 if not found
     */
    @GetMapping("/{id}")
    public ResponseEntity<DeptVO> getInfo(@PathVariable Integer id) {
        log.info("Query department info by id：{}", id);
        DeptVO dept = deptService.getById(id);
        return ResponseEntity.ok(dept);
    }

    /**
     * Create a new department.
     *
     * @param deptSaveDTO department creation payload
     * @return 201 Created with Location header pointing to the new resource
     */
    @Log
    @PostMapping
    public ResponseEntity<DeptVO> add(@Valid @RequestBody DeptSaveDTO deptSaveDTO) {
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
     * @param deptUpdateDTO department update payload
     * @return 200 OK with the updated department; 404 if not found
     */
    @Log
    @PutMapping("/{id}")
    public ResponseEntity<DeptVO> update(
            @PathVariable Integer id,
            @Valid @RequestBody DeptUpdateDTO deptUpdateDTO) {
        log.info("Update department id={}, payload={}", id, deptUpdateDTO);
        DeptVO updated  = deptService.update(id, deptUpdateDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a department by id.
     *
     * @param id department id
     * <p>
     * 204 No Content on success
     * 404 if not found
     * 409 if the department still has employees
     */
    @Log
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeptById(@PathVariable Integer id) {
        log.info("Delete department by id：{}", id);
        deptService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
