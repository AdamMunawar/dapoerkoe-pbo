package com.dapoerkoe.manajemen_resep.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // <-- TAMBAHKAN ANOTASI INI
@Entity
@Table(name = "resep")
public class Resep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include // <-- DAN TAMBAHKAN ANOTASI INI PADA FIELD 'id'
    private Long id;

    @NotBlank(message = "Nama resep tidak boleh kosong")
    @Size(min = 3, message = "Nama resep minimal 3 karakter")
    private String namaResep;

    @Lob
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

    @ManyToMany(mappedBy = "resepDisimpan", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<User> savedByUsers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "resep_likes",
            joinColumns = @JoinColumn(name = "resep_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private Set<User> likes = new HashSet<>();
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    // Metode helper
    public String getBahanBahanAsHtml() {
        if (this.bahanBahan == null) return "";
        return this.bahanBahan.replaceAll("\n", "<br/>");
    }

    public String getLangkahLangkahAsHtml() {
        if (this.langkahLangkah == null) return "";
        return this.langkahLangkah.replaceAll("\n", "<br/>");
    }
}