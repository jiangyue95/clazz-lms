package com.yue.controller;

import com.yue.pojo.enums.Job;
import com.yue.security.WithMockEmp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("StudentController Integration Tests")
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockEmp(empId = 10, role = Job.HEAD_TEACHER)
    @DisplayName("Head teacher can read a student in their own class -> 200")
    void headTeacher_readOwnClassStudent_returns200() throws Exception {
        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clazzId").value(1));
    }

    @Test
    @WithMockEmp(empId = 10, role = Job.HEAD_TEACHER)
    @DisplayName("Head teacher reading a student outside their class -> 404")
    void headTeacher_readOtherClassStudent_returns404() throws Exception {
        mockMvc.perform(get("/students/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockEmp(empId = 99, role = Job.ADMIN)
    @DisplayName("Admin can read a student in any class -> 200")
    void admin_readOtherClassStudent_returns200() throws Exception {
        mockMvc.perform(get("/students/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2));
    }

    @Test
    @WithMockEmp(empId = 10, role = Job.HEAD_TEACHER)
    @DisplayName("Head teacher deleting a student outside their class -> 404")
    void headTeacher_deleteOtherClassStudent_return404() throws Exception {
        mockMvc.perform(delete("/students/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockEmp(empId = 10, role = Job.HEAD_TEACHER)
    @DisplayName("Head teacher updating a student outside their class -> 404")
    void headTeacher_updateOtherClassStudent_returns404() throws Exception {
        String body = """
                {
                    "name": "newName",
                    "no": "S00000002",
                    "gender": 2,
                    "phone": "13900000002",
                    "idCard": "110101200001010002",
                    "idCollege": 1,
                    "clazzId": 2
                }
                """;

        mockMvc.perform(put("/students/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockEmp(empId = 10, role = Job.HEAD_TEACHER)
    @DisplayName("Head teacher recording a violation for a student outside their class -> 404")
    void headTeacher_recordViolationOtherClass_returns404() throws Exception {
        String body = """
                {
                    "score": 5
                }
                """;

        mockMvc.perform(post("/students/2/violations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockEmp(empId = 10, role = Job.HEAD_TEACHER)
    @DisplayName("Head teacher creating a student in another class -> 403")
    void headTeacher_createStudentInOtherClass_returns403() throws Exception {
        String body = """
                {
                    "name": "newStudent",
                    "no": "S00000099",
                    "gender": 1,
                    "phone": "13900000099",
                    "idCard": "110101200001019999",
                    "isCollege": 1,
                    "clazzId": 2
                }
                """;

        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockEmp(empId = 10, role = Job.HEAD_TEACHER)
    @DisplayName("Head teacher creating a student in their own class -> 201")
    void headTeacher_createStudentInOwnClass_returns201() throws Exception {
        String body = """
                {
                    "name": "newStudent",
                    "no": "S00000999",
                    "gender": 1,
                    "phone": "13900000999",
                    "idCard": "110101200001010999",
                    "isCollege": 1,
                    "clazzId": 1 
                }
                """;
        
        mockMvc.perform(post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.clazzId").value(1));
    }
}
