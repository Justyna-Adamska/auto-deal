package com.example.autodeal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String dashboard() {
        // Metoda obsługująca ścieżkę /admin/dashboard
        return "adminDashboard"; // Nazwa widoku (np. strony HTML)
    }

}
