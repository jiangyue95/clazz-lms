package com.yue.controller;

import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.vo.LoginVO;
import com.yue.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginVO> login(
            @RequestBody EmpLoginDTO dto,
            HttpServletResponse response) {
        log.info("Employee login: {}", dto.getUsername());
        LoginVO loginVO = authService.login(dto, response);
        return ResponseEntity.ok(loginVO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginVO> refresh(HttpServletRequest request, HttpServletResponse response) {
        LoginVO vo = authService.refresh(request, response);
        return ResponseEntity.ok(vo);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.noContent().build();
    }
}
