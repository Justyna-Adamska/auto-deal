package com.example.autodeal.config;

import com.example.autodeal.cart.CartService;
import com.example.autodeal.user.model.UserModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class CustomLogoutHandler implements LogoutHandler {

    @Autowired
    private CartService cartService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Integer userId = ((UserModel) authentication.getPrincipal()).getId();
        cartService.clearCartOnLogout(userId);
    }
}
