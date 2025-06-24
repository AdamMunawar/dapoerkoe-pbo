

package com.dapoerkoe.manajemen_resep.controller;

import com.dapoerkoe.manajemen_resep.model.User;
import com.dapoerkoe.manajemen_resep.repository.UserRepository;
import com.dapoerkoe.manajemen_resep.service.ResepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LikeController {

    @Autowired
    private ResepService resepService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/resep/{id}/like")
    public ResponseEntity<?> toggleLike(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        resepService.toggleLike(id, user);

        // Mengembalikan status OK tanpa body, frontend akan handle UI update
        return ResponseEntity.ok().build();
    }
}