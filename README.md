# 🍳 Dapoer-koe: Aplikasi Web Manajemen Resep
Dalam Rangka Memenuhi UAS Pemorograman Berorientasi Objek
Dosen Pengampu : Aldy Rialdy Atmadja, M.T
PBO Kelas C-Kelompok 12 :
1. Muhammad Adam Sirojuddin Munawar
2. Muhammad Raditya Raihan
3. Sayyid Maula Rafsanjani
   
Dapoer-koe adalah aplikasi web full-stack yang dibangun menggunakan Java dan Spring Boot. Aplikasi ini berfungsi sebagai platform bagi pengguna untuk membagikan, menemukan, dan mengelola resep masakan. Proyek ini dirancang dengan arsitektur modern, menerapkan prinsip-prinsip desain SOLID, dan dilengkapi dengan berbagai fitur interaktif.

# ✨ Fitur Utama
## Untuk Pengguna
Registrasi & Login: Sistem otentikasi aman menggunakan Spring Security, mendukung login via username, email, atau nomor telepon.

Manajemen Resep (CRUD): Pengguna dapat membuat, membaca, mengedit, dan menghapus resep miliknya sendiri.

Upload Gambar: Fitur untuk mengunggah gambar pada resep dan kategori.

Fitur Interaktif: Sistem "Suka" (Like) dan "Simpan" (Bookmark) resep berbasis AJAX untuk pengalaman pengguna yang dinamis tanpa reload.

Manajemen Profil: Pengguna dapat mengatur data profil dan mengubah password.

Navigasi Berbasis Kategori: Menampilkan resep berdasarkan kategori dengan halaman khusus.

Pencarian: Fitur pencarian resep berdasarkan kata kunci.

Untuk Admin

Manajemen Pengguna (CRUD): Admin dapat menambah, mengedit, dan menghapus data pengguna.

Manajemen Konten Penuh: Admin memiliki wewenang CRUD penuh atas semua resep, kategori, dan slide hero carousel di halaman utama.

# 🚀 Teknologi yang Digunakan
Backend: Java, Spring Boot, Spring Data JPA, Spring Security

Frontend: Thymeleaf, HTML5, CSS3, JavaScript

Database: H2 (untuk pengembangan), MySQL (untuk produksi)

Styling & UI: Bootstrap 5, Bootswatch (Tema Minty)

Library Tambahan: Lombok, Font Awesome, Chart.js, DataTables.js

# 🎨 Tampilan Aplikasi

![image](https://github.com/user-attachments/assets/f64b0d75-def2-4e69-a18b-4a149f9a7650)
![Screenshot (661)](https://github.com/user-attachments/assets/2168720f-87be-46af-b927-2f4e342ebbd4)
![Screenshot (660)](https://github.com/user-attachments/assets/6905405e-d1e2-436e-b5e5-5d2d98607e69)
![Screenshot (662)](https://github.com/user-attachments/assets/05d117a8-ae5b-42e3-a0f4-72e8f700eedf)
![Screenshot (663)](https://github.com/user-attachments/assets/8d3944fd-4a77-48e2-8039-5e0bc90292f7)
![Screenshot (664)](https://github.com/user-attachments/assets/20d8e387-3911-47b4-8ff2-26193b7453c0)
![Screenshot (665)](https://github.com/user-attachments/assets/2ea06f2c-81c0-442f-9cdd-9d9432dd9e1d)

Layout Sidebar Interaktif: Navigasi utama menggunakan sidebar yang bisa dibuka/ditutup untuk pengalaman pengguna yang modern.

Halaman Utama Dinamis: Menampilkan konten yang berbeda untuk tamu, pengguna biasa, dan admin. Termasuk hero carousel yang dapat dikelola admin, serta bagian "Resep Terpopuler" dan "Resep Terbaru".

Desain Responsif: Tampilan dioptimalkan untuk berbagai ukuran layar, dari desktop hingga mobile.
