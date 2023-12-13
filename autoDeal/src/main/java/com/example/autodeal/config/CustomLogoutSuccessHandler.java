package com.example.autodeal.config;

import com.example.autodeal.cart.CartService;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import java.io.IOException;

public class CustomLogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    private final CartService cartService;

    private final UserService userService;

    public CustomLogoutSuccessHandler(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }


    @Override
    public void onLogoutSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException, ServletException {
        final String refererUrl = request.getHeader("Referer");
        System.out.println(refererUrl);
        logout(authentication);
        response.sendRedirect("/login");
        super.onLogoutSuccess(request, response, authentication);
    }

    public void logout(Authentication authentication) {
        if (authentication == null) {
            return;
        }
        String email = authentication.getName();
        UserModel user = userService.findUserByEmail(email);
        Integer userId = user.getId();
        cartService.clearCartOnLogout(userId);
    }
}
