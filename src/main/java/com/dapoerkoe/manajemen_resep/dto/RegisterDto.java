package com.dapoerkoe.manajemen_resep.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterDto {

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 3, max = 20, message = "Username harus 3-20 karakter")
    private String username;

    // Email dan No Telepon tidak wajib diisi keduanya, tapi salah satu harus ada
    // Validasi ini akan kita lakukan di controller
    @Email(message = "Format email tidak valid")
    private String email;

    private String noTelepon;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;

    @NotBlank(message = "Konfirmasi password tidak boleh kosong")
    private String confirmPassword;
}