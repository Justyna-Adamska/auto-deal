package com.example.autodeal.config;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;

import java.io.IOException;
import java.util.UUID;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String sessionId = UUID.randomUUID().toString();
        Cookie sessionCookie = new Cookie("session-id", sessionId);
        sessionCookie.setMaxAge(7 * 24 * 60 * 60); // Ciasteczko ważne przez 7 dni
        sessionCookie.setHttpOnly(true);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
