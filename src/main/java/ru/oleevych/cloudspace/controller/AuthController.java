package ru.oleevych.cloudspace.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.oleevych.cloudspace.dto.UserRegisterDto;
import ru.oleevych.cloudspace.entity.User;
import ru.oleevych.cloudspace.exceptions.PasswordsMismatchException;
import ru.oleevych.cloudspace.exceptions.SignUpException;
import ru.oleevych.cloudspace.service.UserService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        if (!model.containsAttribute("userRegisterDto")) {
            model.addAttribute("userRegisterDto", new UserRegisterDto());
        }
        return "auth/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpUser(@ModelAttribute @Valid UserRegisterDto userDto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/sign-up";
        }

        try {
            userService.registerUser(userDto);
        } catch (SignUpException e) {
            bindingResult.addError(new ObjectError("signUpError", e.getMessage()));
            return "auth/sign-up";
        }
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
