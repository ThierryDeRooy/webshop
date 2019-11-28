package com.webshop.kung.controller;

import com.webshop.kung.entity.User;
import com.webshop.kung.model.UserRegistration;
import com.webshop.kung.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("newUser", new UserRegistration());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("newUser") UserRegistration newUser,
                           BindingResult result) {
        if(result.hasErrors()) {
            return "register";
        }
        userService.saveUser(newUser);
        return "redirect:register?success";

    }
}
