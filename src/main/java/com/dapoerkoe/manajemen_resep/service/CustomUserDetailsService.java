package com.dapoerkoe.manajemen_resep.service;

import com.dapoerkoe.manajemen_resep.model.User;
import com.dapoerkoe.manajemen_resep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // Logikanya menjadi sangat simpel:
        // 1. Cari user berdasarkan username, email, atau no telepon.
        User user = userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(login))
                .or(() -> userRepository.findByNoTelepon(login))
                .orElseThrow(() -> new UsernameNotFoundException("User tidak ditemukan dengan identifier: " + login));
        
        // 2. Langsung kembalikan objek User tersebut.
        // Karena kelas User kita sudah "implements UserDetails", Spring Security akan langsung mengerti.
        return user;
    }
}