package com.dapoerkoe.manajemen_resep.service;

import com.dapoerkoe.manajemen_resep.model.HeroCarouselSlide;
import com.dapoerkoe.manajemen_resep.repository.HeroCarouselSlideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class HeroCarouselSlideService {
    @Autowired private HeroCarouselSlideRepository repository;
    @Value("${upload.path}") private String uploadPath;

    public List<HeroCarouselSlide> findAll() { return repository.findAll(); }
    public List<HeroCarouselSlide> findActiveSlides() { return repository.findAllByAktifTrueOrderByUrutanTampilAsc(); }
    public HeroCarouselSlide findById(Long id) { return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Slide tidak ditemukan")); }
    public void deleteById(Long id) { repository.deleteById(id); }

    public void save(HeroCarouselSlide slide, MultipartFile fileGambar) throws IOException {
        if (fileGambar != null && !fileGambar.isEmpty()) {
            String namaFileUnik = UUID.randomUUID().toString() + "_" + fileGambar.getOriginalFilename();
            Path pathFolderUpload = Paths.get(uploadPath, "hero"); // Simpan di subfolder 'hero'
            Path pathTujuan = pathFolderUpload.resolve(namaFileUnik);

            if (!Files.exists(pathFolderUpload)) {
                Files.createDirectories(pathFolderUpload);
            }
            Files.copy(fileGambar.getInputStream(), pathTujuan, StandardCopyOption.REPLACE_EXISTING);
            slide.setNamaGambar(namaFileUnik);
        }
        repository.save(slide);
    }
}