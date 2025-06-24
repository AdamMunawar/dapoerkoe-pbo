document.addEventListener('DOMContentLoaded', function() {
    const likeButton = document.getElementById('like-button');
    if (!likeButton) return; // Keluar jika tombol tidak ditemukan

    // Ambil CSRF token dari meta tag yang ada di HTML
    const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    likeButton.addEventListener('click', function() {
        const resepId = this.getAttribute('data-resep-id');
        
        // Siapkan header untuk permintaan POST
        const headers = new Headers();
        headers.append(header, token);

        fetch(`/resep/${resepId}/like`, {
            method: 'POST',
            headers: headers
        })
        .then(response => {
            if (response.ok) {
                // Jika berhasil, refresh halaman untuk melihat perubahan
                window.location.reload(); 
            } else {
                // Jika gagal (misal: belum login), beri peringatan
                alert('Anda harus login untuk menyukai resep ini.');
                window.location.href = '/login';
            }
        })
        .catch(error => {
            console.error('Error saat melakukan like:', error);
            alert('Gagal terhubung ke server.');
        });
    });
});