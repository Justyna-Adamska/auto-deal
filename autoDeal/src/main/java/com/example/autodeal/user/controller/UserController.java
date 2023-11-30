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


    @GetMapping("/user/{id}")
//    @GetMapping("/user/{id}")
    public String getaddUser(){
        return "users/addNewUser"; //ustawić odpowiednie widoki (nazwy)
    }

    @PostMapping("/addUser")
    public RedirectView postAddNewUser(UserModel user){
        userService.addUser(user);

        return new RedirectView(("/home"));//po dodaniu uzytkownika przenosi nas na stronę główną
    }

    // /user/{id}       - PUT edit user
    // /user/{id}       - GET self
    @PutMapping("/editUser/{id}")    //musimy upewnić sie, że edytujemy tylko własny id użytkownika
    public RedirectView postEditUser(@PathVariable("id") Integer id, UserModel editUser){
        userService.saveEditUser(editUser);
        return new RedirectView("/home");
    }

    @GetMapping("/getUser/{id}") //pobieramy profil użytkownika po własnym id
    public String getUser(@PathVariable ("id") Integer id, UserModel getUser) {
        userService.findUserById(id);
    return "/user/{id}";
    }
}
