package com.yue.controller;

import com.yue.anno.Log;
import com.yue.pojo.dto.EmpListQueryDTO;
import com.yue.pojo.dto.EmpSaveDTO;
import com.yue.pojo.dto.EmpUpdateDTO;
import com.yue.pojo.PageResult;
import com.yue.pojo.vo.EmpInfoVO;
import com.yue.pojo.vo.EmpVO;
import com.yue.service.EmpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Employee management controller.
 *
 * <p>An "employee" in this system covers all staff role: teachers, head
 * teachers, and advisors. The {@code job} field on the entity distinguishes
 * roles via an enumerated integer.
 *
 * <p>Follows REST conventions: return DTOs directly, uses HTTP status codes
 * for errors (delegated to GlobalExceptionHandler), and leverages proper HTTP
 * semantics (201 Created for POST, 204 No Content for DELETE).
 */
@Tag(
        name = "Employees",
        description = "Employee management (teachers, head teachers, advisors)"
)
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
    @Operation(
            summary = "Page employees with optional filters",
            operationId = "pageEmps"
    )
    @GetMapping
    public ResponseEntity<PageResult<EmpVO>> page(@Valid EmpListQueryDTO empListQueryDTO){
        log.info("Pagination Search: {}", empListQueryDTO);
        PageResult<EmpVO> pageResult = empService.page(empListQueryDTO);
        return ResponseEntity.ok(pageResult);
    }

    /**
     * Add a new employee.
     *
     * @param empSaveDTO employee creation payload
     * @return 201 Created with the new resource and a Location header pointing to it
     */
    @Operation(
            summary = "Create a new employee",
            operationId = "createEmp"
    )
    @Log
    @PostMapping
    public ResponseEntity<EmpInfoVO> save(@Valid @RequestBody EmpSaveDTO empSaveDTO) throws Exception {
        log.info("Add employee: {}", empSaveDTO);
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
    @Operation(
            summary = "Batch delete employees by ids",
            description = "Accept a comma-separated list of employee ids as a " +
                    "query parameter, e.g. DELETE /emps?ids=1,2,3. Best suited " +
                    "for small batches; very large batches may hit URL length" +
                    "limits.",
            operationId = "deleteEmps"
    )
    @Log
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestParam List<Integer> ids) {
        log.info("Delete parameter: {}", ids);
        empService.delete(ids);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get employee info by id.
     *
     * @param id employee id
     * @return 200 OK with employee info; 404 if not found (handled centrally)
     */
    @Operation(
            summary = "Get an employee by id (with work experience)",
            description = "Returns the full employee detail including the " +
                    "nested work-experience list (exprList). For list/page " +
                    "queries use GET /emps which returns a slimmer EmpVO " +
                    "without exprList.",
            operationId = "getEmp"
    )
    @GetMapping("/{id}")
    public ResponseEntity<EmpInfoVO> getInfo(@PathVariable Integer id) {
        log.info("Get employee info by id: {}", id);
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
    @Operation(
            summary = "Update an employee by id",
            operationId = "updateEmp"
    )
    @Log
    @PutMapping("/{id}")
    public ResponseEntity<EmpInfoVO> update(
            @PathVariable Integer id,
            @Valid @RequestBody EmpUpdateDTO empUpdateDTO) {
        log.info("Update employee id={}, payload={}", id, empUpdateDTO);
        EmpInfoVO updated = empService.update(id, empUpdateDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Query all employee basic information.
     *
     * @return 200 OK with all employees(possibly empty)
     */
    @Operation(
            summary = "List all employees (unpaginated)",
            description = "Returns the full list of employees without pagination. " +
                    "Intended for dropdowns and selectors; use GET /emps for " +
                    "paginated browsing.",
            operationId = "listAllEmps"
    )
    @GetMapping("/list")
    public ResponseEntity<List<EmpVO>> getAllEmp() {
        log.info("Query all employee basic information");
        List<EmpVO> allEmps = empService.getAllEmp();
        return ResponseEntity.ok(allEmps);
    }
}
