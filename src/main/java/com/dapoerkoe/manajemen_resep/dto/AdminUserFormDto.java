
package com.dapoerkoe.manajemen_resep.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AdminUserFormDto {
    private Long id;

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 3, max = 20)
    private String username;

    // Password hanya wajib untuk user baru
    @Size(min = 6, message = "Password minimal 6 karakter jika diisi")
    private String password;

    @Email
    private String email;
    private String noTelepon;
    private String namaLengkap;

    @NotBlank(message = "Peran (roles) tidak boleh kosong")
    private String roles; // Misal: ROLE_USER atau ROLE_ADMIN,ROLE_USER
}