package com.dapoerkoe.manajemen_resep.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter // Tambahkan ini
@Setter // Tambahkan ini
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "kategori")
public class Kategori {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nama;

    @OneToMany(mappedBy = "kategori")
    private Set<Resep> resep;
}