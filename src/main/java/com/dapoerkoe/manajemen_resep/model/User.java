package com.dapoerkoe.manajemen_resep.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter // Tambahkan ini
@Setter // Tambahkan ini
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Resep> resep;

     @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String noTelepon;

    // Tambahkan field untuk profil
    private String namaLengkap;
    private String roles;
    @Lob
    private String bio;
}