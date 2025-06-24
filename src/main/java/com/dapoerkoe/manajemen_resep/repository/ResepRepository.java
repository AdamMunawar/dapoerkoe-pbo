package com.dapoerkoe.manajemen_resep.repository;
import com.dapoerkoe.manajemen_resep.model.Resep;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;


public interface ResepRepository extends JpaRepository<Resep, Long> {
    List<Resep> findByNamaResepContainingIgnoreCase(String keyword);

 @Query("SELECT r FROM Resep r JOIN r.likes l GROUP BY r.id ORDER BY COUNT(l) DESC")
List<Resep> findTopPopular(Pageable pageable);
       // TAMBAHKAN METODE INI
    // Spring Data JPA secara otomatis akan membuat query untuk mencari Resep
    // berdasarkan properti 'nama' dari objek 'kategori' yang ada di dalamnya.
    List<Resep> findAllByKategori_NamaIgnoreCase(String namaKategori);
    List<Resep> findTop4ByOrderByCreatedAtDesc();
}