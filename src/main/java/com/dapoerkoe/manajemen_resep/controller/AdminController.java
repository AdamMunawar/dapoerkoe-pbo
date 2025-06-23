package com.dapoerkoe.manajemen_resep.controller;


import com.dapoerkoe.manajemen_resep.repository.ResepRepository;
import com.dapoerkoe.manajemen_resep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin") // Semua mapping di sini berawal dari /admin
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResepRepository resepRepository;

    @GetMapping("/dashboard")
    public String adminDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin/user-list";
    }

    @GetMapping("/recipes")
    public String listRecipes(Model model) {
        model.addAttribute("recipes", resepRepository.findAll());
        return "admin/recipe-list";
    }
}