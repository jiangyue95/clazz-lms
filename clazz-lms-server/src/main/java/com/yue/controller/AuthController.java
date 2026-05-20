package com.yue.controller;

import com.yue.pojo.dto.EmpChangePasswordDTO;
import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.dto.EmpRegisterDTO;
import com.yue.pojo.dto.RefreshDTO;
import com.yue.pojo.vo.EmpInfoVO;
import com.yue.pojo.vo.EmpLoginVO;
import com.yue.pojo.vo.RefreshVO;
import com.yue.service.EmpService;
import com.yue.utils.BaseContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Authentication REST controller.
 *
 * <p>Hosts authentication-related endpoints: login, and (in subsequent commits)
 * registration and password change. Renamed from {@code LoginController} when
 * the scope expanded beyond login alone.
 *
 * <p>Follows REST conventions: returns the {@link EmpLoginVO} resource directly
 * via {@link ResponseEntity}, uses HTTP status codes for errors (delegated to
 * {@code GlobalExceptionHandler}), and uses {@code @Valid} to enforce input
 * constraints declared on the DTO.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final EmpService empService;

    /**
     * Authenticate an employee with username and password.
     *
     * @param dto login credentials
     * @return 200 OK with {@link EmpLoginVO} containing the JWT token;
     *         401 Unauthorized if credentials are invalid (handler centrally);
     */
    @PostMapping("/login")
    public ResponseEntity<EmpLoginVO> login(@Valid @RequestBody EmpLoginDTO dto) {
        log.info("Employee login attempt: {}", dto.getUsername());
        EmpLoginVO vo = empService.login(dto);
        return ResponseEntity.ok(vo);
    }

    /**
     * Get a new access token using the refresh token.
     *
     * @param dto refresh token
     * @return 200 OK with {@link RefreshVO} containing the new access token;
     */
    @PostMapping("/refresh")
    public RefreshVO refresh(@Validated @RequestBody RefreshDTO dto) {
        return empService.refresh(dto);
    }

    @PostMapping("/register")
    public ResponseEntity<EmpInfoVO> register(@Valid @RequestBody EmpRegisterDTO dto) {
        log.info("Registration attempt for username: {}", dto.getUsername());
        EmpInfoVO created = empService.register(dto);
        URI location = URI.create("/emps/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    /**
     * Change the currently-authenticated employee's password.
     *
     * <p>The employee id is taken from the JWT token's claims, not from the
     * request body - this prevents privilege escalation (changing other
     * user's passwords by manipulating the body).
     *
     * @param dto change-password payload (current + new password)
     * @return 204 No Content on success
     */
    @PatchMapping("/me/password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody EmpChangePasswordDTO dto) {
        Integer currentUserId = BaseContext.getCurrentId();
        log.info("Change-password request from empId={}", currentUserId);
        empService.changePassword(currentUserId, dto.getCurrentPassword(), dto.getNewPassword());
        return ResponseEntity.noContent().build();
    }
}
