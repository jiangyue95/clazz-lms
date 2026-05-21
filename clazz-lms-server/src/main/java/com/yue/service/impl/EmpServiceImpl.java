package com.yue.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yue.exception.*;
import com.yue.mapper.EmpExprMapper;
import com.yue.mapper.EmpMapper;
import com.yue.pojo.dto.*;
import com.yue.pojo.entity.Emp;
import com.yue.pojo.entity.EmpExpr;
import com.yue.pojo.entity.EmpLog;
import com.yue.pojo.entity.RefreshToken;
import com.yue.pojo.vo.EmpInfoVO;
import com.yue.pojo.vo.EmpLoginVO;
import com.yue.pojo.vo.EmpVO;
import com.yue.pojo.vo.RefreshVO;
import com.yue.repository.RefreshTokenRepository;
import com.yue.security.JwtConfigProperties;
import com.yue.service.EmpLogService;
import com.yue.service.EmpService;
import com.yue.security.JwtService;
import com.yue.pojo.*;
import com.yue.utils.HashUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmpServiceImpl implements EmpService {

    private final EmpMapper empMapper;
    private final EmpExprMapper empExprMapper;
    private final EmpLogService empLogService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtConfigProperties jwtConfig;

    /**
     * Page query employee list
     * @param dto employee list query DTO
     * @return employee list page result
     */
    @Override
    public PageResult<EmpVO> page(EmpListQueryDTO dto) {
        // 1. set pagination parameters: page number and page size
        PageHelper.startPage(dto.getPage(), dto.getPageSize());

        // 2. execute query to get employee list
        List<EmpVO> empList = empMapper.empList(dto);

        // 3. wrap the query result in a PageResult object
        Page<EmpVO> p = (Page<EmpVO>) empList;
        return new PageResult<>(p.getTotal(), p.getResult());
    }

    /**
     * Save new employee
     * @param empSaveDTO employee save DTO contains employee information and its experience list
     * @throws Exception
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public EmpInfoVO save(EmpSaveDTO empSaveDTO) throws Exception {
        Emp emp = Emp.builder()
                .username(empSaveDTO.getUsername())
                .name(empSaveDTO.getName())
                .gender(empSaveDTO.getGender())
                .job(empSaveDTO.getJob())
                .entryDate(empSaveDTO.getEntryDate())
                .deptId(empSaveDTO.getDeptId())
                .phone(empSaveDTO.getPhone())
                .salary(empSaveDTO.getSalary())
                .image(empSaveDTO.getImage())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .password(passwordEncoder.encode("123456"))
                .build();

        try {
            // 1.Save employee information
            empMapper.insert(emp);

            // 2. save employee work experience
            List<EmpExpr> exprList = empSaveDTO.getExprList();
            if  (!CollectionUtils.isEmpty(exprList)) {
                exprList.forEach(empExpr -> {
                    empExpr.setEmpId(emp.getId());
                });
                empExprMapper.insertBatch(exprList);
            }
        }finally {
            // 3. record operation log in database
            EmpLog empLog = EmpLog.builder()
                    .operateTime(LocalDateTime.now())
                    .info("Add new employee：" + empSaveDTO.toString())
                    .build();
            empLogService.insert(empLog);
        }

        // 4. return the created employee
        return getInfo(emp.getId());
    }

    /**
     * Delete employee by a list of id
     * @param ids a list of employee id
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void delete(List<Integer> ids) {
        // 1. Batch delete employee basic information
        empMapper.deleteByIds(ids);

        // 2. Batch delete employee work experience
        empExprMapper.deleteByEmpIds(ids);
    }

    /**
     * Get employee information by id
     *
     * @param id employee id
     * @return an employee information VO
     */
    @Override
    public EmpInfoVO getInfo(Integer id) {
        Emp emp = empMapper.getById(id);
        if (emp == null) {
            throw new ResourceNotFoundException("Employee not found: " + id);
        }
        List<EmpExpr> exprList = empExprMapper.selectByEmpId(id);

        EmpInfoVO empInfoVO = EmpInfoVO.builder()
                .id(emp.getId())
                .username(emp.getUsername())
                .name(emp.getName())
                .gender(emp.getGender())
                .image(emp.getImage())
                .deptId(emp.getDeptId())
                .entryDate(emp.getEntryDate())
                .job(emp.getJob())
                .salary(emp.getSalary())
                .exprList(exprList)
                .createTime(emp.getCreateTime())
                .updateTime(emp.getUpdateTime())
                .build();
        return empInfoVO;
    }

    /**
     * Update employee information
     * @param empUpdateDTO employee update DTO contains employee information and its experience list
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public EmpInfoVO update(Integer id, EmpUpdateDTO empUpdateDTO) {
        // 1. check if the employee exists; throw 404 if not
        Emp existingEmp = empMapper.getById(id);
        if (existingEmp == null) {
            throw new ResourceNotFoundException("Employee not found: " + id);
        }

        // 2. update employee basic information
        Emp emp = Emp.builder()
                .id(id)
                .username(empUpdateDTO.getUsername())
                .name(empUpdateDTO.getName())
                .gender(empUpdateDTO.getGender())
                .job(empUpdateDTO.getJob())
                .entryDate(empUpdateDTO.getEntryDate())
                .deptId(empUpdateDTO.getDeptId())
                .phone(empUpdateDTO.getPhone())
                .salary(empUpdateDTO.getSalary())
                .image(empUpdateDTO.getImage())
                .updateTime(LocalDateTime.now())
                .build();
        empMapper.updateById(emp);

        // 3. delete the old work experience
        empExprMapper.deleteByEmpIds(Arrays.asList(id));

        // 4. insert new work experience into database
        List<EmpExpr> exprList = empUpdateDTO.getExprList();
        if (!CollectionUtils.isEmpty(exprList)) {
            exprList.forEach(empExpr -> empExpr.setEmpId(id));
            empExprMapper.insertBatch(exprList);
        }

        // 5. return the updated employee
        return getInfo(id);
    }

    /**
     * query all employee basic information
     * @return a list of employee basic information
     */
    @Override
    public List<EmpVO> getAllEmp() {
        return empMapper.queryAll();
    }

    /**
     * Login employee by username and password
     *
     * @param dto login request body
     * @return employee login vo with JWT token
     * @throws InvalidCredentialsException if username doesn't exist or password is wrong
     */
    @Override
    public EmpLoginVO login(EmpLoginDTO dto) {
        // 1. select employee by username
        Emp emp = empMapper.getByUsername(dto.getUsername());

        // 2. check credentials
        //    Note: log the precise reason internally for debugging,
        //    but expose only a vague message externally to prevent
        //    user enumeration attacks.
        if (emp == null) {
            log.warn("Login failed: username not found: {}", dto.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(dto.getPassword(), emp.getPassword())) {
            log.warn("Login failed: incorrect password for username: {}", dto.getUsername());
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // 3. generate access token
        Map<String, Object> accessClaims = new HashMap<>();
        accessClaims.put("id", emp.getId());
        accessClaims.put("username", emp.getUsername());
        String accessToken = jwtService.generateAccessToken(accessClaims);

        // 4. generate refresh token + persist to Redis (extracted into a helper)
        String refreshToken = issueRefreshToken(emp);

        // 5. construct and return vo
        return EmpLoginVO.builder()
                .id(emp.getId())
                .username(emp.getUsername())
                .name(emp.getName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public RefreshVO refresh(RefreshDTO dto) {
        String rawRefreshToken = dto.getRefreshToken();

        // 1. Parse + verify signature (catch expired separately)
        Claims claims;
        try {
            claims = jwtService.parseToken(rawRefreshToken);
        } catch (ExpiredJwtException e) {
            log.info("Refresh failed: refresh token expired");
            throw new RefreshTokenExpiredException("Refresh token has expired; please log in again");
        } catch (JwtException e) {
            log.info("Refresh failed: invalid JWT - {}", e.getMessage());
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        // 2. SHA-256 hash and lookup in Redis
        String tokenHash = HashUtil.sha256Hex(rawRefreshToken);
        RefreshToken stored = refreshTokenRepository.findByTokenHash(tokenHash);

        // 3. Validate stored record (handles null, revoked, expired)
        if (!refreshTokenRepository.isValid(stored)) {
            log.info("Refresh failed: token not found, revoked, or expired in Redis");
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        // 4. Re-query emp from DB (Trade-off 5 - fresh username, fail if deleted)
        Integer empId = stored.getEmpId();
        Emp emp = empMapper.getById(empId);
        if (emp == null) {
            log.warn("Refresh failed: refresh token's empId {} no longer exist", empId);
            throw new InvalidRefreshTokenException("Invalid refresh token");
        }

        // 5. Generate new access token (not rotating refresh - deferred follow-up)
        Map<String, Object> accessClaims = new HashMap<>();
        accessClaims.put("id", emp.getId());
        accessClaims.put("username", emp.getUsername());
        String newAccessToken = jwtService.generateAccessToken(accessClaims);

        return RefreshVO.builder()
                .accessToken(newAccessToken)
                .build();
    }

    /**
     * Generate a refresh token and persist its hash to Redis
     *
     * <p>The raw refresh token is returned to the client (exactly once); only
     * its SHA-256 hash is stored server-side. A Redis save failure causes login
     * to fail fast - the dual-token contract promises both tokens, and
     * returning only access would create an inconsistent client state.
     *
     * @param emp the authenticated employee
     * @return the raw refresh token (to embed in the login response)
     * @throws IllegalStateException if Redis persistence fails
     */
    private String issueRefreshToken(Emp emp) {
        // Minimal claims - refresh token's only job is identifying who can
        // exchange it for a new access token. Username will be re-fetched at
        // /auth/refresh time so a stale value can't slip through.
        Map<String, Object> refreshClaims = new HashMap<>();
        refreshClaims.put("id", emp.getId());
        String rawRefreshToken = jwtService.generateRefreshToken(refreshClaims);

        // Store only the hash, never the raw token (Redis compromise leaks
        // hashes, not usable credentials).
        String tokenHash = HashUtil.sha256Hex(rawRefreshToken);
        LocalDateTime now = LocalDateTime.now();
        long refreshTtlMs = jwtConfig.getRefreshExpirationMs();
        RefreshToken record = RefreshToken.builder()
                .tokenHash(tokenHash)
                .empId(emp.getId())
                .issuedAt(now)
                .expiresAt(now.plus(refreshTtlMs, ChronoUnit.MILLIS))
                .revokedAt(null)
                .build();
        refreshTokenRepository.save(record);

        return rawRefreshToken;
    }

    /**
     * Register a new employee with the minimal user-controlled fields.
     *
     * <p>Admin-controlled fields (dep_id, job, salary) are deliberately left
     * as NULL - they must be set later by an admin via {@code PUT /emps/{id}}.
     * Allowing self-registration to set these would be a privilege-escalation
     * vector (a user could assign themselves to any department or set their
     * own salary).
     *
     * <p>Password is hashed via BCrypt befor storage; never stored as
     * plain-text. Duplicate username or phone surfaces as a business rule
     * violation (409 Conflect), not as a database-level error.
     *
     * @param dto registration data
     * @return information about the newly created employee
     * @throws BusinessRuleViolationException if username or phone exists
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public EmpInfoVO register(EmpRegisterDTO dto) {
        // 1. Check duplicate username (cheap pre-check to give a clear 409)
        Emp existingByUsername = empMapper.getByUsername(dto.getUsername());
        if (existingByUsername != null) {
            throw new BusinessRuleViolationException(
                    "Username '" + dto.getUsername() + "' is already taken");
        }

        // 2. Build entity with admin-controlled fields left NULL
        Emp emp = Emp.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .gender(dto.getGender())
                .phone(dto.getPhone())
                // deptId, job, salary, image, entryDate: left null
                // - admin must set them via PUT /emps/{id} afterward
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();

        // 3. Insert；phone uniqueness is enforced by DB unique index.
        //    If phone collides, the SQL constraint violation surfaces as
        //    DataIntegrityViolationException - caught here and rethrown as
        //    BusinessRuleViolationException for a clean 409 response.
        try {
            empMapper.insert(emp);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Registration failed: integrity violation for username={} phone={}",
                    dto.getUsername(), dto.getPhone());
            throw new BusinessRuleViolationException("Username or phone is already taken");
        }

        log.info("Registered new employee: id={} username={}", emp.getId(), emp.getUsername());

        // 4. Return the created employee's info
        return getInfo(emp.getId());
    }

    /**
     * Change an authenticated employee's password.
     * 
     * <p>Verifies the user-provided current password against the stored hash
     * before applying the change. Even with a valid auth token, knowing the
     * current password is required - this is a defense against session hijack,
     * where a stolen token alone shouldn't be enough to lock out the legitimate
     * user.
     * 
     * <p>The empId argument comes from the authenticated user's token, never
     * from the request body - allowing the body to specify the user would be
     * a privilege - escalation vector (anyone could change anyone's password).
     * 
     * @param empId authenticated employee's id (from JWT token)
     * @param currentPassword user-provided current password
     * @param newPassword new password (will be BCrypt-hashed)
     * @throws ResourceNotFoundException if empId doesn't match a real user
     *                                    (defensive - shouldn't happen if
     *                                    the token is valid)
     * @throws InvalidCredentialsException if currentPassword is wrong
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changePassword(Integer empId, String currentPassword, String newPassword) {
        // 1. Load the employee (defensive - empId should be valid per token,
        //    but verify rather than trust
        Emp emp = empMapper.getById(empId);
        if (emp == null) {
            log.warn("Change-password attempt for non-existent empId = {}", empId);
            throw new ResourceNotFoundException("Emp with id" + empId + " not found");
        }

        // 2. Verify the current password before accepting the change
        if (!passwordEncoder.matches(currentPassword, emp.getPassword())) {
            log.warn("Change-password failed: incorrect currentPassword for empId={}", empId);
            throw new InvalidCredentialsException("Current password is incorrect");
        }

        // 3. Hash the new password and update
        String newHash = passwordEncoder.encode(newPassword);
        empMapper.updatePassword(empId, newHash);
        log.info("Password changed for empId={} username={}", empId, emp.getUsername());

        // 4. Revoke all refresh tokens for this employee.
        //    IMPORTANT: This must be the LAST operation in the method
        //    @Transactional rolls back DB on uncaught exceptions, but Redis is
        //    not transactional, if any code below this point throws, DB would
        //    roll back (password unchanged) while Redis would not (tokens
        //    revoked). The try-catch swallows Redis failure so nothing
        //    propagates; do not add code after it that could throw.
        try {
            int revoked = refreshTokenRepository.revokeAllByEmpId(empId);
            log.info("Revoked {} refresh tokens of employee:{} in Redis", revoked, empId);
        } catch (Exception e) {
            log.error("Password changed for empId={} but refresh token revoke FAILED: {}." +
                    "Old refresh tokens remain valid until natural expiry - manual cleanup may be required.",
                    empId, e.getMessage(), e);
        }
    }
}
