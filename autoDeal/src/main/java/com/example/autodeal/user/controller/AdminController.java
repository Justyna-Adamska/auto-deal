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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/dashboard")
    public String dashboard() {

        return "admin/adminDashboard";
    }

    // Lista wszystkich użytkowników
    @GetMapping("/users")
    public String findAllUsers(Model model){
        List<UserModel> userList = userService.findAllUsers();

        model.addAttribute("userModel", userList);
        return "admin/adminUsers";

    }


    // Szukanie użytkowników
//    @GetMapping("/search")
//    public String searchUsers(@RequestParam String query, Model model) {
//        List<UserModel> searchResults = userService.searchUsers(query);
//        model.addAttribute("users", searchResults);
//        return "userSearchResults";
//    }


    @GetMapping("/{id}") //metoda do przegladania konkretnych profili użytkowników

    public String getUser(@PathVariable("id") Integer id, Model model){
        UserModel user = userService.findUserById(id);
        model.addAttribute("user",user);

        return "admin/userDetails";
    }


    @PostMapping("/editUser/{id}")
    public RedirectView editUser(@PathVariable("id") Integer id, UserModel editUser){
        userService.saveEditUser(editUser);
        return new RedirectView("/admin/users");
    }

    // Usuwanie użytkownika
    @PostMapping("/delete/{id}")
    public RedirectView deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return new RedirectView("/admin/users");
    }

    // Dodawanie nowego użytkownika
    @GetMapping("/addUser")
    public String getAddUser(Model model) {
        model.addAttribute("user", new UserModel());
        return "addUser";

    }

//    @PostMapping("/addUser")
//    public RedirectView postAddUser(UserModel newUser) {
//        userService.addUser(newUser);
//        return new RedirectView("/admin/users");
//    }
}
