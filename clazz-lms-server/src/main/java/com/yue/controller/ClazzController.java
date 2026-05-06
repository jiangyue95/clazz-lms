package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.dto.ClazzSaveDTO;
import com.yue.pojo.dto.ClazzUpdateDTO;
import com.yue.pojo.ClazzQueryParam;
import com.yue.pojo.PageResult;
import com.yue.pojo.vo.ClazzVO;
import com.yue.service.ClazzService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Clazz(class) REST controller.
 *
 * <p>Follows REST conventions: returns DTOs directly, uses HTTP status codes
 * for errors (delegated to GlobalExceptionHandler), and leverages proper HTTP
 * semantics (201 Created for POST, 204 No Content for DELETE).
 */
@Slf4j
@RestController
@RequestMapping("/clazzs")
@RequiredArgsConstructor
public class ClazzController {

    private final ClazzService clazzService;

    /**
     * Pagination query of clazz(class) list.
     *
     * @param clazzQueryParam query parameters
     * @return 200 OK with the paged result (possibly empty)
     */
    @GetMapping
    public ResponseEntity<PageResult<ClazzVO>> page(ClazzQueryParam clazzQueryParam) {
        log.info("Query clazz(class) list by pagination: {}", clazzQueryParam);
        PageResult<ClazzVO> pageResult = clazzService.page(clazzQueryParam);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * Add a new clazz(class).
     *
     * @param clazzSaveDTO clazz save dto
     * @return 201 Created with the new resource and a location header pointing to it
     */
    @Log
    @PostMapping
    public ResponseEntity<ClazzVO> save(@RequestBody ClazzSaveDTO clazzSaveDTO) {
        log.info("Add new clazz(class)：{}", clazzSaveDTO);
        ClazzVO created = clazzService.save(clazzSaveDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Query clazz(class) by id.
     *
     * @param id clazz id
     * @return 200 OK with clazz info; 404 if not found (handled centrally)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClazzVO> getClazzById(@PathVariable Integer id) {
        log.info("Query clazz(class) by id：{}", id);
        ClazzVO clazzVO =  clazzService.getClassById(id);
        return ResponseEntity.ok(clazzVO);
    }

    /**
     * Update clazz(class) by id.
     *
     * @param id clazz id (from URL, authoritative)
     * @param clazzUpdateDTO update payload
     * @return 200 OK with the updated clazz; 404 if not found
     */
    @Log
    @PutMapping("/{id}")
    public ResponseEntity<ClazzVO> modifyClazz(
            @PathVariable Integer id,
            @RequestBody ClazzUpdateDTO clazzUpdateDTO) {
        log.info("Update clazz(class) id={}, payload={}", id, clazzUpdateDTO);
        ClazzVO updated = clazzService.modifyClazz(id, clazzUpdateDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete clazz(class) by id.
     *
     * @param id clazz id
     * @return 204 No Content on success; 404 if not found
     */
    @Log
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeClazzById(@PathVariable Integer id){
        log.info("Delete clazz(class) by id：{}", id);
        clazzService.deleteClazzById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Query all clazz(class) list.
     *
     * @return 200 OK with all clazzs (possibly empty)
     */
    @GetMapping("/list")
    public ResponseEntity<List<ClazzVO>> getAllClazzs() {
        log.info("Query all clazz(class) list");
        List<ClazzVO> clazzList = clazzService.getAllClazzs();
        return ResponseEntity.ok(clazzList);
    }
}
