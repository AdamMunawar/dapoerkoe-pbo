package com.dapoerkoe.manajemen_resep.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class HeroCarouselSlide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String namaGambar;

    @Column(length = 100)
    private String judul;

    @Column(length = 255)
    private String subjudul;

    private int urutanTampil = 0; // Untuk mengurutkan slide

    private boolean aktif = true; // Untuk mengaktifkan/menonaktifkan slide
}