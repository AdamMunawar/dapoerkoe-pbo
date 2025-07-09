package com.dapoerkoe.manajemen_resep.repository;

import com.dapoerkoe.manajemen_resep.model.HeroCarouselSlide;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HeroCarouselSlideRepository extends JpaRepository<HeroCarouselSlide, Long> {
    // Ambil semua slide yang aktif, urutkan berdasarkan urutan tampil
    List<HeroCarouselSlide> findAllByAktifTrueOrderByUrutanTampilAsc();
}