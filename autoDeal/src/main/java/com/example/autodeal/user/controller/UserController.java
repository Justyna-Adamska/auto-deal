package com.example.autodeal.user.controller;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/users")
    public String findAllUsers(Model model){
        List<UserModel> userList = userService.findAllUsers();
        model.addAttribute("userModel", userList);
        return "/userList";
    }

    @GetMapping("/editUser/{id}")
    public String getEditUser(@PathVariable("id") Integer id, Model model)
    {
        UserModel userModel = userService.findUserById(id);

        model.addAttribute("user", userModel);

        return "/editUser";

    }

    @PostMapping("/editUser/{id}")
    public RedirectView postEditUser(@PathVariable("id") Integer id, UserModel editUser){
        userService.saveEditUser(editUser);
        return new RedirectView("/editUser/{id}");
    }

    @PostMapping("delete/{id}")
    public RedirectView deleteUser(@PathVariable("id") Integer id)
    {
        userService.deleteUser(id);
        return  new RedirectView("/home");
    }

}
