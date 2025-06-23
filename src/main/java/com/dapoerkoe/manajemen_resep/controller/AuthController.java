package com.dapoerkoe.manajemen_resep.controller;

import com.dapoerkoe.manajemen_resep.dto.RegisterDto;
import com.dapoerkoe.manajemen_resep.model.User;
import com.dapoerkoe.manajemen_resep.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Metode ini WAJIB ada untuk menampilkan halaman login
    @GetMapping("/login")
    public String viewLoginPage() {
        return "login";
    }

    // Metode ini WAJIB ada untuk menampilkan form registrasi
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new RegisterDto());
        return "register";
    }

    // Metode ini untuk memproses data registrasi
    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("userDto") RegisterDto userDto,
                               BindingResult bindingResult, Model model) {

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userDto", "confirmPassword", "Konfirmasi password tidak cocok."));
        }
        if ((userDto.getEmail() == null || userDto.getEmail().isEmpty()) && (userDto.getNoTelepon() == null || userDto.getNoTelepon().isEmpty())) {
            bindingResult.addError(new FieldError("userDto", "email", "Email atau Nomor Telepon harus diisi salah satu."));
        }
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            bindingResult.addError(new FieldError("userDto", "username", "Username sudah terdaftar."));
        }
        if (userDto.getEmail() != null && !userDto.getEmail().isEmpty() && userRepository.findByEmail(userDto.getEmail()).isPresent()) {
            bindingResult.addError(new FieldError("userDto", "email", "Email sudah terdaftar."));
        }
        if (userDto.getNoTelepon() != null && !userDto.getNoTelepon().isEmpty() && userRepository.findByNoTelepon(userDto.getNoTelepon()).isPresent()) {
            bindingResult.addError(new FieldError("userDto", "noTelepon", "Nomor Telepon sudah terdaftar."));
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        User newUser = new User();
        newUser.setUsername(userDto.getUsername());
        newUser.setEmail(userDto.getEmail());
        newUser.setNoTelepon(userDto.getNoTelepon());
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        newUser.setRoles("ROLE_USER");
        userRepository.save(newUser);

        return "redirect:/login?success";
    }
}