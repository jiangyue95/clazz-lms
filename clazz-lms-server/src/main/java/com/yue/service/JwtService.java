package com.yue.service;

import com.yue.pojo.entity.Emp;

public interface JwtService {
    String generateAccessToken(Emp emp);
    Long parseEmpId(String token);
    boolean validateAccessToken(String token);
}
