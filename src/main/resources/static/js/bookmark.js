document.addEventListener('DOMContentLoaded', function() {
    const saveButton = document.getElementById('save-button');
    // Jika tombol 'Simpan' tidak ada di halaman ini, hentikan eksekusi script
    if (!saveButton) {
        return; 
    }

    // Ambil token keamanan (CSRF) dari meta tag di HTML
    const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    // Tambahkan event listener untuk saat tombol diklik
    saveButton.addEventListener('click', function() {
        const resepId = this.getAttribute('data-resep-id');
        const icon = this.querySelector('i'); // Kita ambil elemen ikon di dalam tombol

        // Siapkan header untuk permintaan POST yang aman
        const headers = new Headers();
        headers.append(header, token);

        // Lakukan permintaan AJAX ke server
        fetch(`/resep/${resepId}/simpan`, {
            method: 'POST',
            headers: headers
        })
        .then(response => {
            // Jika respons tidak OK (misal: 403 Forbidden karena belum login), lempar error
            if (!response.ok) {
                throw new Error('Gagal menyimpan resep. Mungkin Anda belum login.');
            }
            // Ubah respons menjadi format JSON
            return response.json();
        })
        .then(data => {
            // 'data' adalah respons dari BookmarkController kita, contoh: { "saved": true }
            
            // Perbarui tampilan ikon berdasarkan data dari server TANPA reload
            if (data.saved) {
                // Jika berhasil disimpan, ubah ikon menjadi terisi (solid)
                icon.classList.remove('far'); // Hapus class ikon kosong
                icon.classList.add('fas', 'text-primary'); // Tambah class ikon terisi dan beri warna
            } else {
                // Jika batal disimpan, kembalikan ikon menjadi kosong (outline)
                icon.classList.remove('fas', 'text-primary'); // Hapus class ikon terisi dan warna
                icon.classList.add('far'); // Tambah class ikon kosong
            }
        })
        .catch(error => {
            // Jika terjadi error (misalnya karena belum login), cetak error di console
            // dan arahkan pengguna ke halaman login.
            console.error('Error:', error);
            alert('Anda harus login untuk menyimpan resep.');
            window.location.href = '/login';
        });
    });
});