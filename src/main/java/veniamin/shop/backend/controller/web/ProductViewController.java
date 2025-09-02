package veniamin.shop.backend.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductViewController {
    @GetMapping("/products")
    public String products(Model model) {
        return "products";
    }

    @GetMapping("/products/{id}")
    public String productDetails(@PathVariable Long id, Model model) {
        model.addAttribute("productId", id);
        return "product-details";
    }
}

