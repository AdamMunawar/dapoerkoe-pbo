package com.dapoerkoe.manajemen_resep.config;

import com.dapoerkoe.manajemen_resep.model.Kategori;
import com.dapoerkoe.manajemen_resep.repository.KategoriRepository;
import com.dapoerkoe.manajemen_resep.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import com.dapoerkoe.manajemen_resep.model.User;


@Component
public class DataInitializer implements CommandLineRunner {
    private final KategoriRepository kategoriRepository;
    private final UserRepository userRepository; // Tambahkan
    private final PasswordEncoder passwordEncoder; // Tambahkan

    // Update constructor
    public DataInitializer(KategoriRepository kategoriRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.kategoriRepository = kategoriRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        if (kategoriRepository.count() == 0) {
            kategoriRepository.saveAll(Arrays.asList(
                    Kategori.builder().nama("Hidangan Utama").build(),
                    Kategori.builder().nama("Hidangan Penutup").build(),
                    Kategori.builder().nama("Kue & Roti").build(),
                    Kategori.builder().nama("Minuman").build(),
                    Kategori.builder().nama("Salad").build()
            ));
        }
        // Inisialisasi User jika belum ada
        if (userRepository.count() == 0) {
            // Buat Admin
          User admin = User.builder()
        .username("admin")
        .password(passwordEncoder.encode("admin123"))
        .roles("ROLE_ADMIN,ROLE_USER") // Peran harus mengandung ROLE_ADMIN
        .build();
userRepository.save(admin);
            // Buat User Biasa
            User user = User.builder()
                    .username("user")
                    .password(passwordEncoder.encode("user123"))
                    .roles("ROLE_USER")
                    .build();
            userRepository.save(user);
        }
    }
}
    
