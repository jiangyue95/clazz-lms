package com.yue.controller;

import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.Result;
import com.yue.pojo.vo.EmpLoginVO;
import com.yue.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private EmpService empService;

    @PostMapping
    public Result login(@RequestBody EmpLoginDTO dto) {
        log.info("Employee login: {}", dto.getUsername());
        EmpLoginVO vo = empService.login(dto);

        if (vo == null) {
            return Result.error("Wrong username or password");
        }

        return Result.success(vo);
    }
}
