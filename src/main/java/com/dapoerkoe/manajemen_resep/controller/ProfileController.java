package com.dapoerkoe.manajemen_resep.controller;

// ... (semua impor yang dibutuhkan)
import  com.dapoerkoe.manajemen_resep.model.User;
import  com.dapoerkoe.manajemen_resep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User getCurrentUser(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new IllegalStateException("User tidak ditemukan"));
    }

    @GetMapping
    public String viewProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getCurrentUser(userDetails);
        model.addAttribute("user", currentUser);
        return "profile";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute User userForm, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        User currentUser = getCurrentUser(userDetails);
        currentUser.setNamaLengkap(userForm.getNamaLengkap());
        currentUser.setBio(userForm.getBio());
        userRepository.save(currentUser);

        redirectAttributes.addFlashAttribute("successMessage", "Profil berhasil diperbarui!");
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {

        User currentUser = getCurrentUser(userDetails);

        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password lama salah.");
            return "redirect:/profile";
        }
        if (newPassword.length() < 6) {
             redirectAttributes.addFlashAttribute("errorMessage", "Password baru minimal 6 karakter.");
            return "redirect:/profile";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Konfirmasi password baru tidak cocok.");
            return "redirect:/profile";
        }

        currentUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(currentUser);
        redirectAttributes.addFlashAttribute("successMessage", "Password berhasil diubah.");
        return "redirect:/profile";
    }
    @GetMapping("/resep-disimpan")
    public String viewSavedRecipes(Model model, @AuthenticationPrincipal UserDetails userDetails) {
    User currentUser = getCurrentUser(userDetails);
    model.addAttribute("listResep", currentUser.getResepDisimpan());
    return "resep-disimpan";
}
}