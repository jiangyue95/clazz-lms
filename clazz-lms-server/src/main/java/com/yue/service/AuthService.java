package com.yue.service;

import com.yue.pojo.dto.EmpLoginDTO;
import com.yue.pojo.vo.LoginVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    LoginVO login(EmpLoginDTO empLoginDTO,  HttpServletResponse response);
    LoginVO refresh(HttpServletRequest request, HttpServletResponse response);
    void logout(HttpServletRequest request, HttpServletResponse response);
}
