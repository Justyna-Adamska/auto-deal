package com.example.autodeal.user.controller;

import com.example.autodeal.user.dto.SignUpDto;
import com.example.autodeal.user.service.NotificationService;
import com.example.autodeal.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class HomeControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private HomeController homeController;

    @BeforeEach
    void setUp() {
        openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Test
    void whenRegistration_thenReturnsRegistrationView() {
        String viewName = homeController.registration();
        assertEquals("home/registration", viewName);
    }

    @Test
    void whenRegisterNewUser_thenRedirectsAndShowsSuccess() {
        SignUpDto signUpDto = new SignUpDto();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String viewName = homeController.registerNewUser(signUpDto, response, redirectAttributes);

        assertEquals("redirect:/registration-success", viewName);
        verify(userService).registerNewUser(signUpDto, response);
        verify(redirectAttributes).addFlashAttribute("success", "User is registered successfully!");
    }

    @Test
    void whenConfirmRegistration_thenReturnsAccountVerifiedView() {
        String token = "token";

        String viewName = homeController.confirmRegistration(token);

        assertEquals("home/accountVerified", viewName);
        verify(userService).confirmUserRegistration(token);
    }


    @Test
    void whenLogin_thenReturnsLoginView() {
        String viewName = homeController.login();
        assertEquals("home/login", viewName);
    }

    @Test
    void whenAdmin_thenReturnsAdminDashboardView() {
        String viewName = homeController.admin();
        assertEquals("admin/adminDashboard", viewName);
    }

    @Test
    void whenLogout_thenRedirectsToLogin() {
        String viewName = homeController.logout();
        assertEquals("redirect:/login", viewName);
    }

    @Test
    void whenShowForgotPasswordForm_thenReturnsForgotPasswordView() {
        String viewName = homeController.showForgotPasswordForm();
        assertEquals("home/forgotPassword", viewName);
    }

    @Test
    void whenHandleForgotPassword_thenRedirectsAndShowsMessage() {
        String email = "user@example.com";

        when(notificationService.requestPasswordReset(email)).thenReturn("SUCCESS");

        String viewName = homeController.handleForgotPassword(email, redirectAttributes);

        assertEquals("redirect:/login", viewName);
        verify(notificationService).requestPasswordReset(email);
        verify(redirectAttributes).addFlashAttribute("message", "Password reset link has been sent to the provided email address.");
    }

    @Test
    void whenHandleResetPassword_thenRedirectsAndShowsMessage() {
        String token = "token";
        String password = "password";

        when(notificationService.resetPassword(token, password)).thenReturn("SUCCESS");

        String viewName = homeController.handleResetPassword(token, password, redirectAttributes);

        assertEquals("redirect:/login", viewName);
        verify(notificationService).resetPassword(token, password);
        verify(redirectAttributes).addFlashAttribute("message", "Your password has been reset successfully.");
    }
    @Test
    void whenHome_thenReturnsHomeViewWithAttributes() {
        when(authentication.getName()).thenReturn("testUser");

        Collection authorities = Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        when(authentication.getAuthorities()).thenReturn(authorities);

        String viewName = homeController.home(model);

        assertEquals("home", viewName);
        verify(model).addAttribute("username", "testUser");
        verify(model).addAttribute(eq("userRole"), anyString());
    }

}
