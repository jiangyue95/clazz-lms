package com.yue.service;

import com.yue.exception.InvalidCredentialsException;
import com.yue.exception.RefreshTokenExpiredException;
import com.yue.exception.ResourceNotFoundException;
import com.yue.pojo.dto.*;
import com.yue.pojo.PageResult;
import com.yue.pojo.vo.EmpInfoVO;
import com.yue.pojo.vo.EmpLoginVO;
import com.yue.pojo.vo.EmpVO;
import com.yue.pojo.vo.RefreshVO;

import java.util.List;

public interface EmpService {
    /**
     * page query employee list
     * @param dto employee list query DTO contains query parameter
     * @return an employee VO
     */
    PageResult<EmpVO> page(EmpListQueryDTO dto);

    /**
     * Save a new employee.
     *
     * @param empSaveDTO employee save DTO contains employee information and its experience list
     * @return the created employee with its generated id
     */
    EmpInfoVO save(EmpSaveDTO empSaveDTO) throws Exception;

    /**
     * Delete employee by a list of id
     * @param ids a list of employee id
     */
    void delete(List<Integer> ids);

    /**
     * Get employee information by id
     *
     * @param id employee id
     * @return an employee information VO
     */
    EmpInfoVO getInfo(Integer id);

    /**
     * Update the employee identified by the given id.
     *
     * @param id employee id (from URL, authoritative)
     * @param empUpdateDTO update payload
     * @return the updated employee
     */
    EmpInfoVO update(Integer id, EmpUpdateDTO empUpdateDTO);

    /**
     * query all employee basic information
     * @return a list of employee basic information
     */
    List<EmpVO> getAllEmp();

    /**
     * Employee login
     * @param dto employee login DTO contains username and password
     * @return an employee login VO
     */
    EmpLoginVO login(EmpLoginDTO dto);

    /**
     * Register a new employee from a self-service registration form.
     *
     * @param dto registration data (validated by controller @Valid)
     * @return information about the newly created employee
     * @throws com.yue.exception.BusinessRuleViolationException if username or phone already taken
     */
    EmpInfoVO register(EmpRegisterDTO dto);

    /**
     * Change the currently-authenticated employee's password.
     *
     * @param empId the authenticated employee's id (from JWT token)
     * @param currentPassword the user-provided current password for verification
     * @param newPassword the new password (will be BCrypt-hashed before storge)
     * @throws InvalidCredentialsException if currentPassword doesn't match
     * @throws ResourceNotFoundException if empId doesn't exist (defensive)
     */
    void changePassword(Integer empId, String currentPassword, String newPassword);

    /**
     * Exchange a refresh token for a new access token.
     *
     * @param dto refresh token request
     * @return new access token wrapped in {@link RefreshVO}
     * @throws RefreshTokenExpiredException if the refresh token has expired
     * @throws InvalidCredentialsException for all other validation failures
     */
    RefreshVO refresh(RefreshDTO dto);
}
