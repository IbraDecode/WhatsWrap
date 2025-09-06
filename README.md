# WhatsCloneWeb

**Developer: Ibra Decode || Inspired by Meta Whatsapp**

*Seperti Wa Asli Tapi Ini Resmi Wa Web Perangkat Tertaut*

## Deskripsi

WhatsCloneWeb adalah aplikasi Android Native (Java) yang membungkus WhatsApp Web dalam WebView dengan tampilan yang disulap agar mirip WhatsApp Mobile. Aplikasi ini mendukung login dengan QR code dan dengan nomor telepon (OTP pairing) sebagaimana sudah tersedia di WhatsApp Web resmi.

## Fitur Utama

### 1. WebView WhatsApp Web
- Memuat https://web.whatsapp.com/ dengan konfigurasi optimal
- JavaScript, DOM storage, File access, dan Wide viewport diaktifkan
- User-agent desktop untuk kompatibilitas penuh
- Injeksi CSS & JavaScript untuk tampilan mobile-like

### 2. Tampilan Mobile-Like
- Sidebar kiri disembunyikan (daftar chat & menu desktop)
- Panel chat ditampilkan full width
- Header chat tinggi 56dp dengan warna hijau WhatsApp (#075E54)
- Input pesan sticky di bawah
- Background chat abu-abu dengan doodle seperti WhatsApp Mobile
- Bubble chat dengan styling mobile: padding 8dp, radius 16dp

### 3. Floating Action Button (FAB)
- FAB di kanan bawah untuk kirim pesan baru
- Dialog Material Design dengan input nomor dan pesan
- Otomatis membuka chat di WhatsApp Web
- Menyimpan riwayat chat ke SharedPreferences

### 4. Riwayat Chat
- Menampilkan daftar riwayat chat tersimpan
- RecyclerView dengan tampilan card
- Klik item untuk membuka chat
- Long press untuk hapus dari riwayat

### 5. Toolbar Menu
- Reload: refresh WebView
- Riwayat: buka HistoryActivity
- Keluar: close app dengan finishAffinity()

### 6. Login Support
- QR code scan (default)
- Tautkan dengan nomor telepon (OTP pairing)
- Menggunakan flow resmi WhatsApp Web tanpa backend tambahan

## Struktur Proyek

```
WhatsCloneWeb/
├── app/
│   ├── src/main/
│   │   ├── java/com/ibradecode/whatscloneweb/
│   │   │   ├── MainActivity.java
│   │   │   ├── HistoryActivity.java
│   │   │   ├── HistoryAdapter.java
│   │   │   └── HistoryItem.java
│   │   ├── res/
│   │   │   ├── layout/
│   │   │   │   ├── activity_main.xml
│   │   │   │   ├── activity_history.xml
│   │   │   │   ├── item_history.xml
│   │   │   │   └── dialog_send_message.xml
│   │   │   ├── menu/
│   │   │   │   └── menu_main.xml
│   │   │   ├── values/
│   │   │   │   ├── colors.xml
│   │   │   │   ├── strings.xml
│   │   │   │   └── themes.xml
│   │   │   └── drawable/
│   │   │       ├── ic_chat.xml
│   │   │       ├── ic_refresh.xml
│   │   │       └── ic_history.xml
│   │   └── AndroidManifest.xml
│   ├── build.gradle
│   └── proguard-rules.pro
├── build.gradle
├── settings.gradle
└── gradle.properties
```

## Dependencies

- androidx.appcompat:appcompat:1.6.1
- com.google.android.material:material:1.9.0
- androidx.constraintlayout:constraintlayout:2.1.4
- androidx.coordinatorlayout:coordinatorlayout:1.2.0
- androidx.recyclerview:recyclerview:1.3.1

## Permissions

- INTERNET: Akses internet untuk WhatsApp Web
- ACCESS_NETWORK_STATE: Cek status jaringan
- CAMERA: Untuk scan QR code
- RECORD_AUDIO: Untuk voice message
- MODIFY_AUDIO_SETTINGS: Pengaturan audio
- WRITE_EXTERNAL_STORAGE: Simpan file
- READ_EXTERNAL_STORAGE: Baca file

## Cara Kompilasi

1. Buka proyek di Android Studio
2. Sync Gradle files
3. Build > Generate Signed Bundle / APK
4. Pilih APK dan ikuti wizard untuk signing
5. APK siap untuk instalasi

## Cara Penggunaan

1. Install APK di perangkat Android
2. Buka aplikasi WhatsCloneWeb
3. Login menggunakan QR code atau nomor telepon
4. Gunakan FAB untuk kirim pesan baru
5. Akses riwayat melalui menu toolbar
6. Nikmati pengalaman WhatsApp Web dengan tampilan mobile

## Catatan Penting

- Aplikasi ini adalah wrapper resmi WhatsApp Web
- Tidak ada modifikasi pada protokol atau keamanan WhatsApp
- Semua fitur login dan chat menggunakan sistem resmi Meta/WhatsApp
- Data disimpan sesuai kebijakan WhatsApp Web

## Lisensi

Aplikasi ini dibuat untuk tujuan edukasi dan pengembangan. WhatsApp adalah trademark dari Meta Platforms, Inc.

---

**Developer: Ibra Decode**  
*Inspired by Meta Whatsapp*




## Peningkatan Aplikasi (Manus AI)

Aplikasi WhatsCloneWeb ini telah ditingkatkan dengan berbagai fitur baru dan peningkatan performa, mengubahnya dari aplikasi WebView dasar menjadi klien WhatsApp Web yang lebih fungsional dan ramah pengguna di platform Android.

### Fitur yang Diimplementasikan:

#### 1. Peningkatan Notifikasi
- **Notifikasi Real-time**: Aplikasi sekarang mampu mendeteksi pesan baru dari WhatsApp Web dan menampilkan notifikasi real-time di perangkat Android. Ini dicapai dengan menyuntikkan JavaScript (`MutationObserver`) ke dalam WebView untuk memantau perubahan DOM yang mengindikasikan pesan baru, dan kemudian memicu notifikasi Android melalui `JavascriptInterface`.

#### 2. Dukungan Multi-Akun (Multi-Instance)
- **Manajemen Akun Terisolasi**: Aplikasi kini mendukung pengelolaan beberapa akun WhatsApp Web. Setiap akun memiliki data sesi (cookie, penyimpanan lokal) yang terisolasi, memungkinkan pengguna untuk beralih antar akun tanpa perlu login ulang. Data akun disimpan dan dimuat secara manual menggunakan `SharedPreferences`.

#### 3. Pengaturan Aplikasi Lanjutan
- **Halaman Pengaturan Baru**: Ditambahkan halaman pengaturan komprehensif yang memungkinkan pengguna untuk mengontrol berbagai aspek aplikasi:
    - **Mode Gelap/Terang**: Opsi untuk mengubah tema visual aplikasi.
    - **Ukuran Font**: Pengguna dapat menyesuaikan ukuran font di WebView untuk kenyamanan membaca.
    - **Manajemen Data**: Opsi untuk membersihkan data aplikasi (misalnya, cache WebView, cookie).
    - **Pengalih User-Agent**: Pengguna dapat memilih user-agent (Desktop Windows, Mobile Android, Mobile iOS) untuk mengubah cara WhatsApp Web merender tampilannya.

#### 4. Peningkatan Performa dan Stabilitas
- **Optimasi WebView**: Pengaturan WebView telah dioptimalkan untuk performa yang lebih baik, termasuk penggunaan akselerasi hardware.
- **Penanganan Error yang Lebih Baik**: Implementasi `WebViewClient` yang ditingkatkan untuk menangani error pemuatan halaman dan proses WebView yang crash atau dihentikan oleh OS, dengan pesan Toast yang informatif dan upaya reload otomatis.
- **Injeksi Skrip Efisien**: Kode JavaScript dan CSS kustom yang sebelumnya disematkan langsung di `MainActivity` kini dipindahkan ke file terpisah (`custom_css.js` dan `message_monitor.js`) di folder `res/raw`. Ini meningkatkan keterbacaan kode, mempermudah pemeliharaan, dan berpotensi mempercepat waktu kompilasi.

#### 5. Integrasi Fitur Native (Opsional)
- **Akses Kamera & Mikrofon**: Aplikasi sekarang dapat meminta izin kamera dan mikrofon saat WebView memerlukannya (misalnya, untuk panggilan video atau pengiriman media). Penanganan izin runtime telah ditambahkan menggunakan `ActivityCompat` dan `ContextCompat`.
- **Akses File**: `WebChromeClient` telah dikonfigurasi untuk menangani `onShowFileChooser`, memungkinkan pengguna untuk mengunggah file dari perangkat mereka ke WhatsApp Web.
- **Geolocation**: Izin geolokasi ditangani untuk memungkinkan WhatsApp Web mengakses lokasi pengguna jika diperlukan.

#### 6. Peningkatan UI/UX (Lanjutan)
- **Splash Screen**: Aplikasi kini memiliki splash screen saat pertama kali diluncurkan, memberikan pengalaman startup yang lebih mulus dan profesional.
- **Indikator Loading**: `ProgressBar` telah ditambahkan ke `MainActivity` yang terlihat saat WebView memuat halaman dan disembunyikan setelah pemuatan selesai, memberikan umpan balik visual kepada pengguna.
- **Perbaikan Tampilan Dialog**: Dialog pengiriman pesan (`dialog_send_message.xml`) telah diperbarui untuk menggunakan komponen Material Design yang lebih modern (`MaterialTextView`) dan skema warna yang konsisten dengan tema aplikasi.


