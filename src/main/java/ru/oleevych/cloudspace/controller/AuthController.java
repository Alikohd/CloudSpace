package ru.oleevych.cloudspace.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.oleevych.cloudspace.entity.User;
import ru.oleevych.cloudspace.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/sign-in";
    }

    @GetMapping("/users")
    public String showUsers(Model model) {
        List<User> users = userService.getUsers();
        model.addAttribute("users", users);
        return "auth/main";
    }

    @GetMapping("/sign-in")
    public String signInForm() {
        return "auth/sign-in";
    }
}
