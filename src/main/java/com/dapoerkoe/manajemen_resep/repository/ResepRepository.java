package com.dapoerkoe.manajemen_resep.repository;
import com.dapoerkoe.manajemen_resep.model.Resep;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResepRepository extends JpaRepository<Resep, Long> {
    List<Resep> findByNamaResepContainingIgnoreCase(String keyword);
}