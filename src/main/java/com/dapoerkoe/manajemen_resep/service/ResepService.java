package com.dapoerkoe.manajemen_resep.service;

import com.dapoerkoe.manajemen_resep.model.Resep;
import com.dapoerkoe.manajemen_resep.model.User;
import com.dapoerkoe.manajemen_resep.repository.ResepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResepService {
    @Autowired private ResepRepository resepRepository;
    @Value("${upload.path}") private String uploadPath;

    public List<Resep> searchAndFilter(String keyword, String kategori) {
        boolean hasKeyword = StringUtils.hasText(keyword);
        boolean hasKategori = StringUtils.hasText(kategori);

        if (hasKategori) {
            return resepRepository.findAllByKategori_NamaIgnoreCase(kategori);
        } else if (hasKeyword) {
            return resepRepository.findByNamaResepContainingIgnoreCase(keyword);
        } else {
            return resepRepository.findAll();
        }
    }

    public Optional<Resep> getResepById(Long id) { return resepRepository.findById(id); }

    @Transactional
    public Resep saveOrUpdateResep(Resep resep, MultipartFile fileGambar) throws IOException {
        if (fileGambar != null && !fileGambar.isEmpty()) {
            String namaFileUnik = UUID.randomUUID().toString() + "_" + fileGambar.getOriginalFilename();
            Path pathFolderUpload = Paths.get(uploadPath);
            Path pathTujuan = pathFolderUpload.resolve(namaFileUnik);
            if (!Files.exists(pathFolderUpload)) {
                Files.createDirectories(pathFolderUpload);
            }
            Files.copy(fileGambar.getInputStream(), pathTujuan, StandardCopyOption.REPLACE_EXISTING);
            resep.setNamaGambar(namaFileUnik);
        }
        return resepRepository.save(resep);
    }

    public void deleteResepById(Long id) { resepRepository.deleteById(id); }

    @Transactional
    public void toggleLike(Long resepId, User user) {
        Resep resep = resepRepository.findById(resepId)
                .orElseThrow(() -> new IllegalArgumentException("Resep tidak ditemukan"));
        if (resep.getLikes().contains(user)) {
            resep.getLikes().remove(user);
        } else {
            resep.getLikes().add(user);
        }
    }

    public List<Resep> findTop4Popular() {
        return resepRepository.findTopPopular(PageRequest.of(0, 4));
    }

    public List<Resep> findLatest4Recipes() {
        return resepRepository.findTop4ByOrderByCreatedAtDesc();
    }
}