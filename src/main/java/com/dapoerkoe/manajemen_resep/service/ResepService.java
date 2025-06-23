package com.dapoerkoe.manajemen_resep.service;

import com.dapoerkoe.manajemen_resep.model.Resep;
import com.dapoerkoe.manajemen_resep.repository.ResepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class ResepService {
    @Autowired private ResepRepository resepRepository;
    @Value("${upload.path}") private String uploadPath;

    public List<Resep> search(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return resepRepository.findByNamaResepContainingIgnoreCase(keyword);
        }
        return resepRepository.findAll();
    }

    public Optional<Resep> getResepById(Long id) { return resepRepository.findById(id); }

    
public Resep saveOrUpdateResep(Resep resep, MultipartFile fileGambar) throws IOException {
    if (fileGambar != null && !fileGambar.isEmpty()) {
        String namaFileUnik = UUID.randomUUID().toString() + "_" + fileGambar.getOriginalFilename();

        // Gunakan .resolve() untuk menggabungkan path, ini lebih aman.
        Path pathFolderUpload = Paths.get(uploadPath);
        Path pathTujuan = pathFolderUpload.resolve(namaFileUnik);

        // Pastikan direktori ada
        if (!Files.exists(pathFolderUpload)) {
            Files.createDirectories(pathFolderUpload);
        }

        Files.copy(fileGambar.getInputStream(), pathTujuan, StandardCopyOption.REPLACE_EXISTING);
        resep.setNamaGambar(namaFileUnik);
    }
    return resepRepository.save(resep);
}

    public void deleteResepById(Long id) { resepRepository.deleteById(id); }
}