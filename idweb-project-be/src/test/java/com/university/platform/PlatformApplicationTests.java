package com.university.platform;

import com.university.platform.service.CourseService;
import com.university.platform.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.apache.http.entity.ContentType.DEFAULT_BINARY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@EnableWebMvc
class PlatformApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CourseService courseService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;


    @Test
    void contextLoads() {
    }

    private static final MockMultipartFile A_FILE =
            new MockMultipartFile("thumbnail", null, DEFAULT_BINARY.toString(), "Employee Record".getBytes());


    @Test
    @WithMockUser
    void testMe() throws Exception {
        MockMultipartFile employeeJson = new MockMultipartFile("course", null,
                "application/json", "{\"courseTitle\": \"Emp Name\"}".getBytes());

        mockMvc.perform(multipart("/api/courses")
                .file(A_FILE)
                .file(employeeJson))
                .andExpect(status().isOk());
    }

    @Test
    void testJu() {
        String s = "hello";
        String s1 = new String("My message{0}");
        System.out.println(String.format(s1, s));
    }
}

