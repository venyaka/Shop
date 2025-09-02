package veniamin.shop.backend.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller

public class MainController {
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        return "register";
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam(required = false) String token, @RequestParam(required = false) String email, Model model) {
        model.addAttribute("token", token);
        model.addAttribute("email", email);
        return "confirm";
    }
}

