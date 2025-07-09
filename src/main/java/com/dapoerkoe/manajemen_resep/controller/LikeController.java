package com.dapoerkoe.manajemen_resep.controller;

import com.dapoerkoe.manajemen_resep.model.Resep;
import com.dapoerkoe.manajemen_resep.model.User;
import com.dapoerkoe.manajemen_resep.repository.ResepRepository;
import com.dapoerkoe.manajemen_resep.repository.UserRepository;
import com.dapoerkoe.manajemen_resep.service.ResepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LikeController {

    @Autowired
    private ResepService resepService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ResepRepository resepRepository; // Kita butuh ini untuk mengambil data terbaru

    @PostMapping("/resep/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        
        // Lakukan aksi suka/batal suka
        resepService.toggleLike(id, user);

        // Ambil kembali data resep yang sudah terupdate untuk mendapatkan jumlah suka terbaru
        Resep updatedResep = resepRepository.findById(id).orElseThrow();
        int likeCount = updatedResep.getLikes().size();
        boolean isLiked = updatedResep.getLikes().contains(user);

        // Kembalikan status baru sebagai JSON
        return ResponseEntity.ok(Map.of(
                "liked", isLiked,
                "likeCount", likeCount
        ));
    }
}