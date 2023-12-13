package com.example.autodeal.user.controller;

import com.example.autodeal.order.service.OrderService;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

//@WebMvcTest(UserController.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private UserService userService;
    @MockBean
    private OrderService orderService;

    @MockBean
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void userProfile_ShouldDisplayUserProfile() throws Exception {
        UserModel user = new UserModel();
        user.setEmail("test@example.com");

        when(authentication.getName()).thenReturn(user.getEmail());
        when(userService.findUserByEmail(anyString())).thenReturn(user);

        mockMvc.perform(get("/user/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/userProfile"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void showEditFormFragment_ShouldReturnEditForm() throws Exception {
        UserModel user = new UserModel();
        user.setEmail("test@example.com");

        when(authentication.getName()).thenReturn(user.getEmail());
        when(userService.findUserByEmail(anyString())).thenReturn(user);

        mockMvc.perform(get("/user/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("fragments/editProfileForm"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void saveEditedUser_ShouldRedirectToProfile() throws Exception {
        mockMvc.perform(post("/user/edit")
                                .param("email", "test@example.com")

                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/profile"));

        verify(userService, times(1)).saveEditUser(any(UserModel.class));
    }

    @Test
    void deleteUserAccount_WhenUserExists_ShouldRedirectToLogout() throws Exception {
        UserModel user = new UserModel();
        user.setId(1);
        user.setEmail("test@example.com");

        when(authentication.getName()).thenReturn(user.getEmail());
        when(userService.findUserById(user.getId())).thenReturn(user);

        mockMvc.perform(post("/user/delete/" + user.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(userService, times(1)).deleteUser(user.getId());
    }

    @Test
    void deleteUserAccount_WhenUserNotExists_ShouldRedirectToErrorPage() throws Exception {
        Integer userId = 1;
        when(userService.findUserById(userId)).thenReturn(null);
        when(authentication.getName()).thenReturn("otherUser@example.com");

        mockMvc.perform(post("/user/delete/" + userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/errorPage"));
    }
}
