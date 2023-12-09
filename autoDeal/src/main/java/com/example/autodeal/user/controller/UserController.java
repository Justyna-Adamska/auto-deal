package com.example.autodeal.user.controller;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
//wyświetlanie własnego profilu użytkownika
    @GetMapping("/profile")
    public String userProfile() {
        return "user/userProfile";
    }


//możliwość edycji własnych danych przez użytkownika
    @PutMapping("/user/{id}")
    public RedirectView postEditUser(@PathVariable("id") Integer id, UserModel editUser){
        userService.saveEditUser(editUser);
        return new RedirectView("/userProfile");//Przekierowanie na profil uzytkownika
        // aby mógł sprawdzić poprawność edytowanych danych
    }
    //mozliwość aby użytkownik mógł skasować swoje konto
    @PostMapping("/user/delete/{id}")
    public RedirectView deleteUserAccount(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return new RedirectView("/logout");
    }

}
