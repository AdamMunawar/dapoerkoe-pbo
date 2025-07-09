package com.dapoerkoe.manajemen_resep.controller;

import com.dapoerkoe.manajemen_resep.model.Resep;
import com.dapoerkoe.manajemen_resep.model.User;
import com.dapoerkoe.manajemen_resep.repository.UserRepository;
import com.dapoerkoe.manajemen_resep.service.KategoriService;
import com.dapoerkoe.manajemen_resep.service.ResepService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.dapoerkoe.manajemen_resep.service.HeroCarouselSlideService;
import java.io.IOException;
import java.util.List;

@Controller
public class ResepController {

    @Autowired private ResepService resepService;
    @Autowired private KategoriService kategoriService;
    @Autowired private UserRepository userRepository;
    @Autowired HeroCarouselSlideService carouselService;
    /**
     * Metode ini menangani halaman utama ("/")
     * dan mengirimkan SEMUA data yang dibutuhkan oleh index.html
     */
    @GetMapping("/")
    public String viewHomePage(Model model) {
        model.addAttribute("listKategori", kategoriService.getAllKategori());
        model.addAttribute("resepPopuler", resepService.findTop4Popular());
        model.addAttribute("resepTerbaru", resepService.findLatest4Recipes());
        model.addAttribute("slides", carouselService.findActiveSlides());
        return "index";
    }
@GetMapping("/kategori/all")
public String viewAllKategoriPage(Model model) {
    // Ambil semua data kategori dari database
    model.addAttribute("listKategori", kategoriService.getAllKategori());
    // Arahkan ke file HTML baru bernama 'semua-kategori.html'
    return "semua-kategori";
}

    /**
     * Metode untuk menampilkan halaman HASIL PENCARIAN
     */
    @GetMapping("/search")
    public String searchResultPage(@RequestParam(name = "keyword", required = false) String keyword, Model model) {
        model.addAttribute("pageTitle", "Hasil Pencarian untuk: '" + keyword + "'");
        model.addAttribute("keyword", keyword);
        model.addAttribute("listResep", resepService.searchAndFilter(keyword, null));
        return "search-result";
    }

    /**
     * Metode untuk menampilkan halaman DETAIL RESEP
     */
    @GetMapping("/resep/{id}")
   public String viewResepDetail(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
    // Memanggil service yang sudah kita perbaiki sebelumnya (findByIdWithDetails)
    Resep resep = resepService.findByIdWithDetails(id)
            .orElseThrow(() -> new IllegalArgumentException("ID Resep tidak valid: " + id));

    boolean isSaved = false;
    boolean isLiked = false;

    if (userDetails != null) {
        User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        // Sekarang perbandingan ini akan bekerja dengan benar!
        isSaved = currentUser.getResepDisimpan().contains(resep); 
        isLiked = resep.getLikes().contains(currentUser);
    }

    model.addAttribute("resep", resep);
    model.addAttribute("isSaved", isSaved);
    model.addAttribute("isLiked", isLiked);
    
    return "resep-detail";
}

    /**
     * Metode untuk menampilkan halaman resep PER KATEGORI
     */
    @GetMapping("/kategori/{nama}")
    public String viewKategoriPage(@PathVariable("nama") String nama, Model model) {
        List<Resep> resepByKategori = resepService.searchAndFilter(null, nama);
        model.addAttribute("namaKategori", nama);
        model.addAttribute("listResep", resepByKategori);
        return "kategori-detail";
    }

    /**
     * Metode untuk menampilkan FORM TAMBAH RESEP
     */
    @GetMapping("/tambah")
    public String showNewRecipeForm(Model model) {
        model.addAttribute("resep", new Resep());
        model.addAttribute("listKategori", kategoriService.getAllKategori());
        model.addAttribute("postUrl", "/simpan"); // URL untuk submit form
        return "form_resep";
    }
    
    /**
     * Metode untuk menampilkan FORM EDIT RESEP
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Resep resep = resepService.getResepById(id).orElseThrow(() -> new IllegalArgumentException("ID Resep tidak valid:" + id));
        // Otorisasi: hanya pemilik resep atau admin yang boleh edit
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!resep.getUser().getUsername().equals(userDetails.getUsername()) && !isAdmin) {
            return "redirect:/?error=Akses ditolak";
        }
        model.addAttribute("resep", resep);
        model.addAttribute("listKategori", kategoriService.getAllKategori());
        model.addAttribute("postUrl", "/simpan");
        return "form_resep";
    }

    /**
     * Metode untuk MEMPROSES penyimpanan resep baru/update
     */
    @PostMapping("/simpan")
    public String saveRecipe(@Valid @ModelAttribute("resep") Resep resep,
                             BindingResult result,
                             @RequestParam("fileGambar") MultipartFile fileGambar,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("listKategori", kategoriService.getAllKategori());
            model.addAttribute("postUrl", "/simpan");
            return "form_resep";
        }
        try {
            // Jika ini resep baru, set pemiliknya
            if (resep.getId() == null) {
                User currentUser = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
                resep.setUser(currentUser);
            } else {
                // Jika update, pertahankan pemilik aslinya
                Resep resepAsli = resepService.getResepById(resep.getId()).orElseThrow();
                resep.setUser(resepAsli.getUser());
            }

            resepService.saveOrUpdateResep(resep, fileGambar);
            redirectAttributes.addFlashAttribute("successMessage", "Resep berhasil disimpan!");
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("listKategori", kategoriService.getAllKategori());
            model.addAttribute("postUrl", "/simpan");
            model.addAttribute("uploadError", "Gagal mengunggah gambar.");
            return "form_resep";
        }
        return "redirect:/";
    }
    
    /**
    * Metode untuk MENGHAPUS resep
    */
    @GetMapping("/hapus/{id}")
    public String deleteResep(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        Resep resep = resepService.getResepById(id).orElseThrow(() -> new IllegalArgumentException("ID Resep tidak valid:" + id));
        // Otorisasi: hanya pemilik resep atau admin yang boleh hapus
        boolean isAdmin = userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!resep.getUser().getUsername().equals(userDetails.getUsername()) && !isAdmin) {
             redirectAttributes.addFlashAttribute("errorMessage", "Akses ditolak!");
            return "redirect:/";
        }
        resepService.deleteResepById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Resep berhasil dihapus!");
        return "redirect:/";
    }
}