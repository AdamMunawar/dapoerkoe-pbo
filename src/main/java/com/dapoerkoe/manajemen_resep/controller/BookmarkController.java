package com.dapoerkoe.manajemen_resep.controller;

import com.dapoerkoe.manajemen_resep.model.*;
import com.dapoerkoe.manajemen_resep.repository.*;
import com.dapoerkoe.manajemen_resep.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class BookmarkController {
    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private ResepRepository resepRepository;

    @PostMapping("/resep/{id}/simpan")
    public ResponseEntity<?> toggleSave(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        Resep resep = resepRepository.findById(id).orElseThrow();

        boolean isSaved = userService.toggleSaveRecipe(user, resep);

        return ResponseEntity.ok(Map.of("saved", isSaved));
    }
}