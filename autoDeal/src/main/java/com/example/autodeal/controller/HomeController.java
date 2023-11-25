package com.example.autodeal.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {
    @GetMapping("/")
    public String greeting() {
        return "index";
    }

    @GetMapping("/home")
    public String home(){ return "home";}

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login";
    }
}
