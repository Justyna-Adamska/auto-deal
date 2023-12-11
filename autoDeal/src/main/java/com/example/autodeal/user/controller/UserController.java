package com.example.autodeal.user.controller;
import com.example.autodeal.user.model.UserModel;
import com.example.autodeal.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
//wyświetlanie własnego profilu użytkownika
@GetMapping("/profile")
public String userProfile(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    UserModel user = userService.findUserByEmail(email);
    model.addAttribute("user", user);
    return "user/userProfile";
}

//możliwość edycji własnych danych przez użytkownika
@GetMapping("/edit")
public String showEditFormFragment(Model model){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName();
    UserModel user = userService.findUserByEmail(email);
    model.addAttribute("user", user);
    return "fragments/editProfileForm";
}
    @PostMapping("/edit")
    public String saveEditedUser(@ModelAttribute UserModel user, Model model) {
        userService.saveEditUser(user);
        model.addAttribute("user", user);
        model.addAttribute("updateSuccess", true);
        return "redirect:/user/profile";
    }


    //mozliwość aby użytkownik mógł skasować swoje konto
    @PostMapping("/delete/{id}")
    public RedirectView deleteUserAccount(@PathVariable("id") Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        UserModel user = userService.findUserById(id);
        if (user != null && user.getEmail().equals(currentUserName)) {
            userService.deleteUser(id);
            return new RedirectView("/logout");
        } else {
            return new RedirectView("/errorPage");
        }
    }

}

