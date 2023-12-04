package com.example.autodeal.user.controller;

import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.NotificationService;
import com.example.autodeal.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {

    private final UserService userService;


    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/adminDashboard";
    }

    @GetMapping("/users")
    public String findAllUsers(Model model){
        List<UserModel> userList = userService.findAllUsers();
        model.addAttribute("userModel", userList);
        return "admin/adminUsers";
    }

    @GetMapping("/{id}") //metoda do przegladania konkretnych profili użytkowników

    public String getUser(@PathVariable("id") Integer id, Model model){
        UserModel user = userService.findUserById(id);
        model.addAttribute("user",user);

        return "admin/userDetails";
    }
    @PostMapping("/addUser")
    public RedirectView postAddNewUser(UserModel user){
        userService.addUser(user);

        return new RedirectView(("/home"));//dodanie użytkownika jest dostępne tylko dla admina
    }


    @PostMapping("/editUser/{id}")
    public RedirectView editUser(@PathVariable("id") Integer id, UserModel editUser){
        userService.saveEditUser(editUser);
        return new RedirectView("/home");
    }

    @PostMapping("delete/{id}")
    public RedirectView deleteUser(@PathVariable("id") Integer id)
    {
        userService.deleteUser(id);
        return  new RedirectView("/logout");
    }

}
