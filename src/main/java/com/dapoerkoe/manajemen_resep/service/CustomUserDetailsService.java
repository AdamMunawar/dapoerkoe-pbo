package com.dapoerkoe.manajemen_resep.service;

import com.dapoerkoe.manajemen_resep.model.User;
import com.dapoerkoe.manajemen_resep.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
      
        User user = userRepository.findByUsername(login)
                .or(() -> userRepository.findByEmail(login))
                .or(() -> userRepository.findByNoTelepon(login))
                .orElseThrow(() -> {
                    System.out.println("DEBUG: GAGAL, User tidak ditemukan dengan identifier: " + login);
                    return new UsernameNotFoundException("User tidak ditemukan dengan identifier: " + login);
                });
        

        Collection<? extends GrantedAuthority> authorities = mapRolesToAuthorities(user.getRoles());
        
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(String roles) {
        System.out.println("DEBUG: Memproses string roles: '" + roles + "'");
        if (roles == null || roles.trim().isEmpty()) {
            System.out.println("DEBUG: Roles kosong, memberikan ROLE_USER default.");
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        }
        Collection<SimpleGrantedAuthority> authorities = Arrays.stream(roles.split(","))
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        return authorities;
    }
}