package com.yue.controller;

import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.vo.EmpLoginVO;
import com.yue.service.EmpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication REST controller.
 *
 * <p>Follows REST conventions: returns the {@link EmpLoginVO} resource directly
 * via {@link ResponseEntity}, uses HTTP status codes for errors (delegated to
 * {@code GlobalExceptionHandler}), and uses {@code @Valid} to enforce input
 * constraints declared on the DTO.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final EmpService empService;

    /**
     * Authenticate an employee with username and password.
     *
     * @param dto login credentials
     * @return 200 OK with {@link EmpLoginVO} containing the JWT token;
     *         401 Unauthorized if credentials are invalid (handler centrally);
     */
    @PostMapping
    public ResponseEntity<EmpLoginVO> login(@Valid @RequestBody EmpLoginDTO dto) {
        log.info("Employee login attempt: {}", dto.getUsername());
        EmpLoginVO vo = empService.login(dto);
        return ResponseEntity.ok(vo);
    }
}
