package com.yue.controller;

import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.Result;
import com.yue.pojo.vo.EmpLoginVO;
import com.yue.service.EmpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class LoginController {

    private final EmpService empService;

    @PostMapping
    public Result login(@RequestBody EmpLoginDTO dto) {
        log.info("Employee login attempt: {}", dto.getUsername());
        EmpLoginVO vo = empService.login(dto);
        return Result.success(vo);
    }
}
