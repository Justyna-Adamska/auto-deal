package com.example.autodeal.user.controller;

import com.example.autodeal.user.dto.SignUpDto;
import com.example.autodeal.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    public void testRegisterUser() throws Exception {
        SignUpDto signUpDto = new SignUpDto(); // Uzupełnij dane
        mockMvc.perform(post("/registration")
                        .contentType("application/json")
                        .content(new ObjectMapper().writeValueAsString(signUpDto)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testConfirmRegistration() throws Exception {
        mockMvc.perform(get("/registrationConfirm")
                        .param("token", "someToken"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testHome() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    public void testLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser
    public void testLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection());
    }
}
