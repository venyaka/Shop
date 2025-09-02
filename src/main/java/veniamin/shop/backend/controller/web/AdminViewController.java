package veniamin.shop.backend.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminViewController {
    @GetMapping("/admin")
    public String adminPanel(Model model) {
        return "admin";
    }

    @GetMapping("/admin/categories")
    public String adminCategories(Model model) {
        return "admin-categories";
    }

    @GetMapping("/admin/products")
    public String adminProducts(Model model) {
        return "admin-products";
    }
}

