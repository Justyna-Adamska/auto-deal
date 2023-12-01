package com.example.autodeal.user.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import com.example.autodeal.user.dto.SignUpDto;
import com.example.autodeal.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringRunner.class)
class HomeControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = standaloneSetup(homeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void whenRegisterUser_thenResponseIsOkAndContentIsCorrect() throws Exception {
        SignUpDto signUpDto = new SignUpDto(); // Populate with test data
        String signUpDtoJson = objectMapper.writeValueAsString(signUpDto);

        mockMvc.perform(post("/registration")
                        .contentType("application/json")
                        .content(signUpDtoJson))
                .andExpect(status().isOk())
                .andExpect(content().string("User is registered successfully!"));
    }

    @Test
    void whenConfirmRegistration_thenViewNameIsAccountVerified() throws Exception {
        mockMvc.perform(get("/registrationConfirm").param("token", "valid-token"))
                .andExpect(status().isOk())
                .andExpect(view().name("accountVerified"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER", "ADMIN"})
    void whenRequestHomePage_thenViewNameIsHome() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("userRole"));
    }
    @Test
    @WithMockUser
    void whenRequestLoginPage_thenViewNameIsLogin() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void whenRequestAdminPage_thenViewNameIsAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminDashboard"));
    }

    @Test
    void whenRequestLogout_thenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}
