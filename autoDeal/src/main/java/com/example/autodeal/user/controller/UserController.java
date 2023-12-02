package com.example.autodeal.user.controller;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    public String userProfile() {
        return "userProfile";
    }





    @PutMapping("/user/{id}")
    public RedirectView postEditUser(@PathVariable("id") Integer id, UserModel editUser){
        userService.saveEditUser(editUser);
        return new RedirectView("/user/profile");//Przekierowanie na profil uzytkownika
        // aby mógł sprawdzić poprawność edytowanych danych
    }

    @PostMapping("/user/delete/{id}") //mozliwość aby użytkownik mógł skasować swoje konto
    public RedirectView deleteUserAccount(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return new RedirectView("/logout");
    }

//    @GetMapping("/user/{id}") //pobieramy profil użytkownika po własnym id
//    public String getUser(@PathVariable ("id") Integer id, Model model) {
//        UserModel user = userService.findUserById(id);
//        model.addAttribute("user", user);
//    return "/user/{id}";
//    }
}
