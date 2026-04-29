package com.yue.controller;

import org.springframework.http.MediaType;
import com.yue.interceptor.TokenInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for {@link DeptController}.
 * <p>
 * These tests start the full Spring application context against an H2
 * in-memory database (configured in application-test.yml). Each test
 * runs in a transaction that's rolled back at the end, keeping test
 * data isolated.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("DeptController Integration Tests")
class DeptControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TokenInterceptor tokenInterceptor;

    @BeforeEach
    void setUp() throws Exception {
        when(tokenInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    // ============================================================
    // GET /depts — list all departments
    // ============================================================

    @Test
    @DisplayName("GET /depts returns 200 with the seeded departments")
    void list_returnsAllDepartments() throws Exception {
        mockMvc.perform(get("/depts"))
                .andDo(print())                                    // 调试: 打印请求和响应
                .andExpect(status().isOk())                        // 状态码 200
                .andExpect(jsonPath("$").isArray())                // 响应是数组
                .andExpect(jsonPath("$.length()").value(3))        // 3 个部门 (来自 data-test.sql)
                .andExpect(jsonPath("$[0].name").value("研发部")); // 第一个是研发部
    }

    // ============================================================
    // GET /depts/{id} — get one department
    // ============================================================

    @Test
    @DisplayName("GET /depts/{id} return 200 with department info when it exists")
    void getById_returnsDepartmentInfo_whenItExists() throws Exception {
        mockMvc.perform(get("/depts/1"))
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("研发部"))
                .andExpect(jsonPath("$.createTime").exists())
                .andExpect(jsonPath("$.updateTime").exists());
    }

    @Test
    @DisplayName("GET /depts/{id} return 404 when department does not exist")
    void getById_returns404_whenItDoesNotExist() throws Exception {
        mockMvc.perform(get("/depts/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.path").value("/depts/99999"))
                .andExpect(jsonPath("$.timestamp").exists());
    }

    // ============================================================
    // POST /depts — create a new department
    // ============================================================

    @Test
    @DisplayName("POST /depts returns 201 Created with Location header for valid input")
    void save_returns201WithLocation_forValidInput() throws Exception {
        String requestBody = "{\"name\":\"产品部\"}";

        mockMvc.perform(post("/depts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/depts/")))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("产品部"))
                .andExpect(jsonPath("$.createTime").exists())
                .andExpect(jsonPath("$.updateTime").exists());
    }

    @Test
    @DisplayName("POST /depts returns 400 when name is empty string")
    void save_returns400_whenNameIsEmpty() throws Exception {
        String requestBody = "{\"name\":\"\"}";

        mockMvc.perform(post("/depts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    @Test
    @DisplayName("POST /depts returns 400 when name is whitespace only")
    void save_returns400_whenNameIsWhitespace() throws Exception {
        String requestBody = "{\"name\":\"   \"}";

        mockMvc.perform(post("/depts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    @Test
    @DisplayName("POST /depts returns 400 when name is too short (1 char)")
    void save_returns400_whenNameTooShort() throws Exception {
        String requestBody = "{\"name\":\"X\"}";

        mockMvc.perform(post("/depts")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    @Test
    @DisplayName("POST /depts returns 400 when name exceeds 50 characters")
    void save_returns400_whenNameTooLong() throws Exception {
        // 51 'A' characters — one over the limit
        String tooLongName = "A".repeat(51);
        String requestBody = "{\"name\":\"" + tooLongName + "\"}";

        mockMvc.perform(post("/depts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    @Test
    @DisplayName("POST /depts returns 400 when name field is missing")
    void save_returns400_whenNameMissing() throws Exception {
        String requestBody = "{}";

        mockMvc.perform(post("/depts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    // ============================================================
    // PUT /depts/{id} — update an existing department
    // ============================================================

    @Test
    @DisplayName("PUT /depts/{id} returns 200 with updated data for valid input")
    void update_returns200WithUpdatedData_forValidInput() throws Exception {
        String requestBody = "{\"name\":\"财务管理部\"}";

        mockMvc.perform(put("/depts/3")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("财务管理部"))
                .andExpect(jsonPath("$.updateTime").exists());
    }

    @Test
    @DisplayName("PUT /depts/{id} returns 404 when department does not exist")
    void update_returns404_forNonexistentId() throws Exception {
        String requestBody = "{\"name\":\"Any name\"}";

        mockMvc.perform(put("/depts/99999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/depts/99999"));
    }

    @Test
    @DisplayName("PUT /depts/{id} returns 400 when name is blank")
    void update_returns400_whenNameIsBlank() throws Exception {
        String requestBody = "{\"name\":\"\"}";

        mockMvc.perform(put("/depts/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"));
    }

    // ============================================================
    // DELETE /depts/{id} — delete a department
    // ============================================================

    @Test
    @DisplayName("DELETE /depts/{id} returns 204 No Content when department has no employees")
    void delete_returns204_whenNoEmployees() throws Exception {
        // 市场部 (id=2) 没有员工, 可以安全删除
        mockMvc.perform(delete("/depts/2"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));     // 204 没有响应体
    }

    @Test
    @DisplayName("DELETE /depts/{id} returns 404 when department does not exist")
    void delete_returns404_forNonexistentId() throws Exception {
        mockMvc.perform(delete("/depts/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.path").value("/depts/99999"));
    }

    @Test
    @DisplayName("DELETE /depts/{id} returns 409 Conflict when department has employees")
    void delete_returns409_whenHasEmployees() throws Exception {
        // 研发部 (id=1) 有员工 alice, 不能删除
        mockMvc.perform(delete("/depts/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("BUSINESS_RULE_VIOLATION"))
                .andExpect(jsonPath("$.message").exists());
    }
}