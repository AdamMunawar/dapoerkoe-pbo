package com.dapoerkoe.manajemen_resep.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import lombok.EqualsAndHashCode;

@Getter // Tambahkan ini
@Setter // Tambahkan ini
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
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