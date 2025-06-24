package com.dapoerkoe.manajemen_resep.controller;


import com.dapoerkoe.manajemen_resep.repository.ResepRepository;
import com.dapoerkoe.manajemen_resep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dapoerkoe.manajemen_resep.model.Kategori;
import com.dapoerkoe.manajemen_resep.model.User;
import com.dapoerkoe.manajemen_resep.repository.KategoriRepository;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import com.dapoerkoe.manajemen_resep.dto.AdminUserFormDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import com.dapoerkoe.manajemen_resep.model.Resep;
import com.dapoerkoe.manajemen_resep.service.KategoriService;
import com.dapoerkoe.manajemen_resep.service.ResepService;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import java.nio.file.*;
import java.util.UUID;




@Controller
@RequestMapping("/admin") // Semua mapping di sini berawal dari /admin
public class AdminController {

    @Value("${upload.path}") // Inject path upload dari application.properties
    private String uploadPath;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResepRepository resepRepository;

    @Autowired
    private KategoriRepository kategoriRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

     @Autowired private KategoriService kategoriService;
    @Autowired private ResepService resepService;


    @GetMapping("/dashboard")
public String adminDashboard() {
    return "admin/dashboard"; // Hanya mengembalikan nama view
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
     @GetMapping("/kategori")
    public String listKategori(Model model) {
        model.addAttribute("listKategori", kategoriRepository.findAll());
        return "admin/kategori-list";
    }

    @GetMapping("/kategori/tambah")
    public String showKategoriForm(Model model) {
        model.addAttribute("kategori", new Kategori());
        return "admin/kategori-form";
    }

    @GetMapping("/kategori/edit/{id}")
    public String showEditKategoriForm(@PathVariable("id") Long id, Model model) {
        Kategori kategori = kategoriRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id Kategori tidak valid:" + id));
        model.addAttribute("kategori", kategori);
        return "admin/kategori-form";
    }

    @PostMapping("/kategori/simpan")
    public String saveKategori(@Valid @ModelAttribute("kategori") Kategori kategori,
                               @RequestParam("fileGambar") MultipartFile fileGambar,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "admin/kategori-form";
        }

        // Logika untuk menyimpan file gambar
        if (fileGambar != null && !fileGambar.isEmpty()) {
            try {
                String namaFileUnik = UUID.randomUUID().toString() + "_" + fileGambar.getOriginalFilename();
                Path pathTujuan = Paths.get(uploadPath).resolve(namaFileUnik);
                Files.createDirectories(pathTujuan.getParent());
                Files.copy(fileGambar.getInputStream(), pathTujuan, StandardCopyOption.REPLACE_EXISTING);
                
                // Set nama file baru ke entitas kategori
                kategori.setNamaGambar(namaFileUnik);
            } catch (IOException e) {
                e.printStackTrace();
                // Tambahkan error handling jika perlu
            }
        }
        
        kategoriRepository.save(kategori);
        return "redirect:/admin/kategori";
    }

    @GetMapping("/kategori/hapus/{id}")
    public String deleteKategori(@PathVariable("id") Long id) {
        // Hati-hati: ini akan menghapus kategori bahkan jika ada resep yang menggunakannya.
        // Untuk sekarang, ini cukup.
        kategoriRepository.deleteById(id);
        return "redirect:/admin/kategori";
    }
     // === MANAJEMEN PENGGUNA ===
    @GetMapping("/users/hapus/{id}")
    public String deleteUser(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetails currentUser) {
        // Mencegah admin menghapus akunnya sendiri
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id User tidak valid:" + id));

        if (currentUser.getUsername().equals(userToDelete.getUsername())) {
            // Sebaiknya gunakan RedirectAttributes untuk pesan error yang lebih baik
            return "redirect:/admin/users?error=self_delete";
        }

        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }
     // === MANAJEMEN PENGGUNA (TAMBAH & EDIT) ===

    @GetMapping("/users/tambah")
    public String showAddUserForm(Model model) {
        model.addAttribute("userDto", new AdminUserFormDto());
        return "admin/user-form";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Id User tidak valid:" + id));
        // Konversi dari Entity ke DTO
        AdminUserFormDto userDto = new AdminUserFormDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setNoTelepon(user.getNoTelepon());
        userDto.setNamaLengkap(user.getNamaLengkap());
        userDto.setRoles(user.getRoles());

        model.addAttribute("userDto", userDto);
        return "admin/user-form";
    }

    @PostMapping("/users/simpan")
    public String saveUser(@Valid @ModelAttribute("userDto") AdminUserFormDto userDto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/user-form";
        }
        
        // Logika untuk user baru (ID null)
        if (userDto.getId() == null) {
            if (userDto.getPassword() == null || userDto.getPassword().isEmpty()) {
                result.addError(new FieldError("userDto", "password", "Password wajib diisi untuk user baru."));
                return "admin/user-form";
            }
            User newUser = new User();
            newUser.setUsername(userDto.getUsername());
            newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            newUser.setEmail(userDto.getEmail());
            newUser.setNoTelepon(userDto.getNoTelepon());
            newUser.setNamaLengkap(userDto.getNamaLengkap());
            newUser.setRoles(userDto.getRoles());
            userRepository.save(newUser);
            redirectAttributes.addFlashAttribute("successMessage", "User baru berhasil ditambahkan!");
        } else { // Logika untuk update user yang sudah ada
            User existingUser = userRepository.findById(userDto.getId()).orElseThrow(() -> new IllegalArgumentException("Id User tidak valid:" + userDto.getId()));
            existingUser.setUsername(userDto.getUsername());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setNoTelepon(userDto.getNoTelepon());
            existingUser.setNamaLengkap(userDto.getNamaLengkap());
            existingUser.setRoles(userDto.getRoles());

            // Update password hanya jika diisi
            if (StringUtils.hasText(userDto.getPassword())) {
                existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
            userRepository.save(existingUser);
            redirectAttributes.addFlashAttribute("successMessage", "Data user berhasil diperbarui!");
        }

        return "redirect:/admin/users";
    }
      // === MANAJEMEN RESEP ===

    @GetMapping("/recipes/tambah")
    public String showAdminAddRecipeForm(Model model) {
        model.addAttribute("resep", new Resep());
        model.addAttribute("listKategori", kategoriService.getAllKategori());
        // Arahkan form untuk submit ke URL admin
        model.addAttribute("postUrl", "/admin/recipes/simpan"); 
        return "form_resep"; // Menggunakan ulang form resep yang sudah ada
    }

    @GetMapping("/recipes/edit/{id}")
    public String showAdminEditRecipeForm(@PathVariable("id") Long id, Model model) {
        Resep resep = resepService.getResepById(id).orElseThrow(() -> new IllegalArgumentException("Id Resep tidak valid:" + id));
        model.addAttribute("resep", resep);
        model.addAttribute("listKategori", kategoriService.getAllKategori());
        model.addAttribute("postUrl", "/admin/recipes/simpan");
        return "form_resep";
    }

    @PostMapping("/recipes/simpan")
    public String saveAdminRecipe(@Valid @ModelAttribute("resep") Resep resep,
                                  BindingResult result,
                                  @RequestParam("fileGambar") MultipartFile fileGambar,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("listKategori", kategoriService.getAllKategori());
            model.addAttribute("postUrl", "/admin/recipes/simpan");
            return "form_resep";
        }

        try {
            // Jika ini resep baru (ID null), pemiliknya adalah admin yang sedang login
            if (resep.getId() == null) {
                User adminUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
                resep.setUser(adminUser);
            } else {
                // Jika mengedit, jangan ubah pemilik aslinya. Ambil pemilik dari DB.
                Resep resepAsli = resepService.getResepById(resep.getId()).orElseThrow();
                resep.setUser(resepAsli.getUser());
            }

            resepService.saveOrUpdateResep(resep, fileGambar);
        } catch (IOException e) {
            // Error handling
            model.addAttribute("listKategori", kategoriService.getAllKategori());
            model.addAttribute("postUrl", "/admin/recipes/simpan");
            model.addAttribute("uploadError", "Gagal mengunggah gambar.");
            return "form_resep";
        }
        return "redirect:/admin/recipes";
    }

    @GetMapping("/recipes/hapus/{id}")
    public String deleteAdminRecipe(@PathVariable("id") Long id) {
        resepService.deleteResepById(id);
        return "redirect:/admin/recipes";
    }


}

