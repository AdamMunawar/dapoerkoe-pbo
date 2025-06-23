package com.dapoerkoe.manajemen_resep.service;
import com.dapoerkoe.manajemen_resep.model.Kategori;
import com.dapoerkoe.manajemen_resep.repository.KategoriRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class KategoriService {
    @Autowired private KategoriRepository kategoriRepository;
    public List<Kategori> getAllKategori() { return kategoriRepository.findAll(); }
}