package com.dapoerkoe.manajemen_resep.service;

import com.dapoerkoe.manajemen_resep.model.Resep;
import com.dapoerkoe.manajemen_resep.model.User;
import com.dapoerkoe.manajemen_resep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public boolean toggleSaveRecipe(User user, Resep resep) {
        // Cek apakah resep sudah ada di daftar simpanan user
        if (user.getResepDisimpan().contains(resep)) {
            user.getResepDisimpan().remove(resep);
            userRepository.save(user);
            return false; // Mengembalikan false karena resepnya di-unsave
        } else {
            user.getResepDisimpan().add(resep);
            userRepository.save(user);
            return true; // Mengembalikan true karena resepnya di-save
        }
    }
}