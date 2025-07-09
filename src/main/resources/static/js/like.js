document.addEventListener('DOMContentLoaded', function() {
    const likeButton = document.getElementById('like-button');
    if (!likeButton) return; // Keluar jika tombol tidak ada

    const likeCountSpan = document.getElementById('like-count'); // Ambil elemen untuk jumlah suka
    const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    likeButton.addEventListener('click', function() {
        const resepId = this.getAttribute('data-resep-id');
        const icon = this.querySelector('i');

        const headers = new Headers();
        headers.append(header, token);

        fetch(`/resep/${resepId}/like`, {
            method: 'POST',
            headers: headers
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Gagal melakukan aksi Suka. Mungkin Anda belum login.');
            }
            return response.json();
        })
        .then(data => {
            // 'data' adalah respons dari LikeController, contoh: { "liked": true, "likeCount": 5 }

            // 1. Update ikon tombol
            if (data.liked) {
                icon.classList.remove('far'); // Hapus ikon hati kosong
                icon.classList.add('fas', 'text-danger'); // Tambah ikon hati penuh + warna merah
            } else {
                icon.classList.remove('fas', 'text-danger'); // Hapus ikon hati penuh + warna
                icon.classList.add('far'); // Tambah ikon hati kosong
            }

            // 2. Update teks jumlah suka
            likeCountSpan.textContent = data.likeCount + ' Suka';
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Anda harus login untuk menyukai resep ini.');
            window.location.href = '/login';
        });
    });
});