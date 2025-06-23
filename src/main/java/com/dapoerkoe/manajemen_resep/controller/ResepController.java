package com.dapoerkoe.manajemen_resep.controller;

import com.dapoerkoe.manajemen_resep.model.*;
import com.dapoerkoe.manajemen_resep.repository.UserRepository;
import com.dapoerkoe.manajemen_resep.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Controller
public class ResepController {
    @Autowired private ResepService resepService;
    @Autowired private KategoriService kategoriService;
    @Autowired private UserRepository userRepository;

    @GetMapping("/")
    public String viewHomePage(Model model, @RequestParam(required = false) String keyword) {
        model.addAttribute("listResep", resepService.search(keyword));
        model.addAttribute("keyword", keyword);
        return "index";
    }

    private void loadFormData(Model model, Resep resep) {
        model.addAttribute("resep", resep);
        model.addAttribute("listKategori", kategoriService.getAllKategori());
    }

    @GetMapping("/tambah")
    public String showNewResepForm(Model model) {
        loadFormData(model, new Resep());
        return "form_resep";
    }

    @PostMapping("/simpan")
    public String saveResep(@Valid @ModelAttribute("resep") Resep resep,
                            BindingResult bindingResult,
                            @RequestParam("fileGambar") MultipartFile fileGambar,
                            @AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (bindingResult.hasErrors()) {
            loadFormData(model, resep);
            return "form_resep";
        }
        try {
            User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
            resep.setUser(currentUser);
            resepService.saveOrUpdateResep(resep, fileGambar);
        } catch (IOException e) {
            // Sebaiknya handle error lebih baik
            e.printStackTrace();
            loadFormData(model, resep);
            model.addAttribute("uploadError", "Gagal mengunggah gambar.");
            return "form_resep";
        }
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Resep resep = resepService.getResepById(id).orElseThrow(() -> new IllegalArgumentException("ID Resep tidak valid:" + id));
        if (!resep.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/?error=unauthorized";
        }
        loadFormData(model, resep);
        return "form_resep";
    }

    @GetMapping("/hapus/{id}")
    public String deleteResep(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Resep resep = resepService.getResepById(id).orElseThrow(() -> new IllegalArgumentException("ID Resep tidak valid:" + id));
        if (!resep.getUser().getUsername().equals(userDetails.getUsername())) {
            return "redirect:/?error=unauthorized";
        }
        resepService.deleteResepById(id);
        return "redirect:/";
    }
}