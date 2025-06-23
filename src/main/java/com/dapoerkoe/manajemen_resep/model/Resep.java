package com.dapoerkoe.manajemen_resep.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.Getter;
import lombok.Setter;

@Getter // Tambahkan ini
@Setter // Tambahkan ini
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "resep")
public class Resep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nama resep tidak boleh kosong")
    @Size(min = 3, message = "Nama resep minimal 3 karakter")
    private String namaResep;

    private String deskripsi;

    @NotBlank(message = "Bahan-bahan tidak boleh kosong")
    @Lob
    private String bahanBahan;

    @NotBlank(message = "Langkah-langkah tidak boleh kosong")
    @Lob
    private String langkahLangkah;

    private String namaGambar;

    @NotNull(message = "Kategori harus dipilih")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kategori_id")
    private Kategori kategori;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}