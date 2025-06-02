````markdown
# Proyek Ujian Akhir Semester (UAS) - Kualitas Perangkat Lunak

## Automasi Tes Aplikasi Mobile BengkelGo Menggunakan Appium dan Java

<p align="center">
  <img src="https://raw.githubusercontent.com/khalidalghifarii/pbm-bengkelgo/main/app/src/main/res/drawable/logo.png" alt="Logo BengkelGo" width="150"/>
</p>

**Disusun Oleh:**

- **Nama:** Muhammad Khalid Al Ghifari
- **NPM:** 2208107010044

---

## 1. Pendahuluan

Proyek ini bertujuan untuk melakukan otomatisasi pengujian (automation testing) pada aplikasi mobile Android "BengkelGo". Pengujian otomatis ini dikembangkan menggunakan framework Appium dengan bahasa pemrograman Java dan TestNG sebagai test runner, serta Maven untuk manajemen proyek dan dependensi. Fokus pengujian adalah pada fungsionalitas inti aplikasi untuk memastikan kualitas dan keandalan perangkat lunak.

## 2. Deskripsi Aplikasi yang Diuji (Application Under Test - AUT)

**BengkelGo** adalah aplikasi mobile Android yang dirancang untuk memudahkan pemilik kendaraan mendapatkan layanan servis dan perbaikan kendaraan langsung di lokasi mereka. Aplikasi ini juga menyediakan fitur panggilan darurat dan toko suku cadang terintegrasi.

**Fitur Utama BengkelGo yang Dicakup dalam Tes (atau menjadi target tes):**

- Manajemen Akun Pengguna (Login, Registrasi).
- Layanan Servis Kendaraan di Lokasi Pelanggan (Pemesanan Servis).
- Panggilan Darurat.
- Toko Suku Cadang (Spare Part Shop).
- Melihat Layanan Terdekat (View Nearest).

Informasi lebih detail mengenai aplikasi BengkelGo dapat ditemukan di repositori sumbernya: [Repositori Aplikasi BengkelGo](https://github.com/khalidalghifarii/pbm-bengkelgo)

## 3. Framework dan Alat yang Digunakan

- **Appium:** Framework open-source untuk otomatisasi pengujian aplikasi mobile (Android & iOS).
- **Java:** Bahasa pemrograman yang digunakan untuk menulis skrip tes.
- **Appium Java Client:** Library Java untuk berinteraksi dengan Appium Server.
- **Selenium WebDriver:** API yang digunakan oleh Appium untuk interaksi dengan elemen UI.
- **TestNG:** Framework testing untuk Java yang digunakan untuk mengatur dan menjalankan skrip tes, serta pelaporan.
- **Maven:** Alat manajemen proyek dan dependensi untuk proyek Java.
- **Android Studio:** IDE untuk pengembangan aplikasi Android dan pembuatan file `.apk` BengkelGo.
- **Visual Studio Code (VSCode):** IDE yang digunakan untuk pengembangan skrip tes Appium dengan Java.
- **Android Emulator/Perangkat Fisik:** Digunakan sebagai target eksekusi tes.

## 4. Struktur Proyek Tes Appium

Proyek otomatisasi tes ini disusun menggunakan Maven dengan struktur direktori standar:

- `pom.xml`: File konfigurasi Maven yang mendefinisikan dependensi proyek (Appium, TestNG, Selenium) dan plugin build.
- `src/test/java/`: Direktori utama untuk kode sumber skrip tes.
  - `com/uas/kpl/`:
    - `LoginTests.java`: Berisi skenario tes untuk fungsionalitas login.
    - `RegistrationTests.java`: Berisi skenario tes untuk fungsionalitas registrasi pengguna baru.
    - `SparePartShopTests.java`: Berisi skenario tes untuk fungsionalitas toko suku cadang.
    - `VehicleServiceOrderTests.java`: Berisi skenario tes untuk fungsionalitas pemesanan servis kendaraan.
    - `EmergencyCallTests.java`: Berisi skenario tes untuk fungsionalitas panggilan darurat.
    - `ViewNearestTests.java`: Berisi skenario tes untuk fungsionalitas melihat layanan terdekat.
- `target/`: Direktori yang berisi hasil build dan laporan tes (misalnya, laporan Surefire).

## 5. Skenario Tes yang Diotomatisasi

Berikut adalah daftar skenario tes utama yang telah diotomatisasi dalam proyek ini:

1.  **Login Pengguna Sukses:**
    - Memastikan pengguna dengan kredensial yang valid dapat berhasil login dan diarahkan ke halaman utama.
2.  **Registrasi Pengguna Baru Sukses:**
    - Memastikan pengguna baru dapat mendaftar dengan data yang valid dan diarahkan ke halaman konfirmasi.
3.  **Pemesanan Servis Kendaraan:**
    - Memastikan pengguna dapat mengisi form pemesanan servis kendaraan (memilih jenis kendaraan, jenis servis, lokasi, jadwal, metode pembayaran), mengisi alamat, dan mengirimkan pesanan hingga ke halaman konfirmasi.
4.  **Penjelajahan Toko Suku Cadang dan Penambahan ke Keranjang:**
    - Memastikan pengguna dapat membuka toko suku cadang, melihat daftar produk (setelah klik "See All"), memilih produk, melihat detail produk (Overview, Features, Specifications), dan menambahkan produk ke keranjang.
5.  **Verifikasi Fitur Panggilan Darurat:**
    - Memastikan dialog panggilan darurat muncul dengan informasi yang benar dan tombol "Hubungi Sekarang" memicu intent ke aplikasi Dialer.
6.  **Membuka Halaman "View Nearest" dan Kembali:**
    - Memastikan pengguna dapat membuka halaman "View Nearest" dan berhasil kembali ke halaman utama.

## 6. Instalasi dan Konfigurasi Appium di macOS (Untuk Presentasi)

Berikut adalah langkah-langkah rinci untuk instalasi dan konfigurasi lingkungan Appium di macOS (seperti yang dilakukan pada Mac Air M2):

### a. Prasyarat Awal

- **Homebrew:** Package manager untuk macOS.
  - Cek instalasi: `brew --version`
  - Instal jika belum: `/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"`
- **Xcode dan Command Line Tools:** Diperlukan untuk beberapa dependensi.
  - Instal Xcode dari App Store.
  - Instal Command Line Tools: `xcode-select --install`

### b. Instalasi Node.js dan npm

Appium dibangun di atas Node.js.

```bash
brew install node
```
````

Verifikasi: `node -v` dan `npm -v`.

### c. Instalasi Java Development Kit (JDK)

Dibutuhkan untuk menjalankan Appium dan proyek tes Java. Versi 11 atau 17 direkomendasikan.

```bash
brew install openjdk@17 # Atau versi lain yang sesuai
```

Setelah instalasi, konfigurasikan `JAVA_HOME` dan `PATH`. Contoh untuk `zsh` (shell default di macOS modern):

```bash
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v17)' >> ~/.zshrc # Atau path manual jika brew tidak mengatur link
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc # Sesuaikan jika path berbeda
source ~/.zshrc
```

Verifikasi: `java -version` dan `echo $JAVA_HOME`.

### d. Instalasi Android Studio dan Android SDK

- Unduh dan instal Android Studio dari [situs resmi Android Developer](https://developer.android.com/studio).
- Melalui SDK Manager di Android Studio (`Settings/Preferences > Appearance & Behavior > System Settings > Android SDK`):
  - Pastikan Android SDK Platform yang sesuai (misalnya, API 33, 34, atau yang digunakan emulator) terinstal.
  - Di tab "SDK Tools", pastikan **"Android SDK Command-line Tools (latest)"** dan **"Android SDK Platform-Tools"** terinstal.
- Konfigurasikan variabel lingkungan `ANDROID_HOME` (atau `ANDROID_SDK_ROOT`):
  ```bash
  # Di ~/.zshrc atau ~/.bash_profile
  export ANDROID_HOME=$HOME/Library/Android/sdk # Ganti dengan lokasi SDK Anda jika berbeda
  export PATH=$PATH:$ANDROID_HOME/platform-tools
  export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin # Untuk sdkmanager, avdmanager
  export PATH=$PATH:$ANDROID_HOME/emulator
  export PATH=$PATH:$ANDROID_HOME/tools # Meskipun 'tools' mulai ditinggalkan, beberapa alat lama mungkin masih ada di sini
  export PATH=$PATH:$ANDROID_HOME/tools/bin
  ```
  Jangan lupa `source ~/.zshrc`.
  Verifikasi: `echo $ANDROID_HOME` dan `adb devices`.

### e. Instalasi Appium Server (Appium 2.x)

```bash
npm install -g appium
```

Verifikasi: `appium --version`.

### f. Instalasi Driver Appium (UiAutomator2 untuk Android)

```bash
appium driver install uiautomator2
```

Verifikasi: `appium driver list`.

### g. Instalasi Appium Inspector

Aplikasi desktop untuk menginspeksi elemen UI aplikasi mobile.

- Unduh versi `.dmg` untuk macOS (arm64 untuk Mac M-series) dari [halaman rilis GitHub Appium Inspector](https://github.com/appium/appium-inspector/releases).
- Instal seperti aplikasi macOS biasa.

### h. (Opsional) Instalasi Alat Bantu Lain

- **Carthage** (jika bekerja dengan driver iOS tertentu): `brew install carthage`
- **Appium Doctor** (untuk diagnosis):
  ```bash
  npm install -g @appium/doctor # Versi terbaru
  appium-doctor --android # Atau appium-doctor saja
  ```
  Perbaiki semua masalah yang ditandai **NECESSARY** oleh `appium-doctor`.

### i. Membuat dan Menjalankan Android Virtual Device (AVD) / Emulator

- Gunakan AVD Manager di Android Studio untuk membuat dan menjalankan emulator. Pastikan emulator menggunakan arsitektur yang sesuai (arm64 untuk Mac M-series lebih disarankan).

## 7\. Persyaratan untuk Menjalankan Proyek Tes

Sebelum menjalankan skrip tes otomatis, pastikan lingkungan Anda memenuhi persyaratan berikut:

- Java JDK (versi 17 atau yang sesuai dengan konfigurasi `pom.xml`) terinstal dan `JAVA_HOME` terkonfigurasi.
- Node.js dan npm terinstal.
- Appium Server (v2.x) terinstal secara global.
- Driver `uiautomator2` untuk Appium terinstal.
- Android Studio dengan Android SDK terinstal dan `ANDROID_HOME` (atau `ANDROID_SDK_ROOT`) terkonfigurasi dengan benar, termasuk `platform-tools` dan `cmdline-tools` di PATH.
- Maven terinstal (VSCode biasanya menangani ini melalui ekstensinya, atau bisa diinstal manual via Homebrew: `brew install maven`).
- Emulator Android yang aktif dan berjalan, atau perangkat Android fisik yang terhubung dengan USB Debugging diaktifkan.
- File `app-debug.apk` dari aplikasi BengkelGo tersedia (dapat di-build dari proyek sumber aplikasi).
- Appium Server **harus dijalankan** secara manual di Terminal sebelum eksekusi tes.

## 8\. Instruksi Menjalankan Tes

1.  **Clone Repositori Proyek Tes:**
    ```bash
    git clone [URL_REPO_GITHUB_ANDA_UNTUK_PROYEK_TES_INI]
    cd [NAMA_FOLDER_PROYEK_TES_ANDA]
    ```
2.  **Pastikan File APK BengkelGo Ada:**
    - Letakkan file `app-debug.apk` BengkelGo di lokasi yang dapat diakses.
    - **PENTING:** Perbarui path absolut ke file `app-debug.apk` di dalam semua file tes Java (`LoginTests.java`, `RegistrationTests.java`, dll.) pada bagian Desired Capabilities:
      ```java
      caps.setCapability("appium:app", "/PATH/LENGKAP/KE/app-debug.apk");
      ```
      Contoh: `/Users/namaanda/proyek/bengkelgo/app-debug.apk`
3.  **Jalankan Appium Server:**
    - Buka Terminal baru.
    - Ketik perintah: `appium`
    - Biarkan jendela Terminal ini terbuka.
4.  **Jalankan Tes Menggunakan Maven:**
    - Buka Terminal baru atau gunakan Terminal terintegrasi di VSCode.
    - Pastikan Anda berada di direktori root proyek tes Appium Anda (direktori yang berisi file `pom.xml`).
    - Untuk menjalankan semua tes:
      ```bash
      mvn clean test
      ```
    - Untuk menjalankan semua tes di kelas tertentu (misalnya, `LoginTests`):
      ```bash
      mvn clean test -Dtest=LoginTests
      ```
    - Untuk menjalankan satu metode tes spesifik di kelas tertentu (misalnya, `testSuccessfulLogin` di `LoginTests`):
      ```bash
      mvn clean test -Dtest=LoginTests#testSuccessfulLogin
      ```
5.  **Lihat Hasil Tes:**
    - Output akan ditampilkan di konsol Maven.
    - Laporan tes lebih detail (Surefire reports) akan tersedia di direktori `target/surefire-reports/`. Buka file `emailable-report.html` atau `index.html` di browser.

## 9\. File `.apk` Aplikasi BengkelGo

File `app-debug.apk` yang digunakan untuk pengujian ini dapat ditemukan di repositori ini pada path:

- `apk/app-debug.apk`

## 10\. (Opsional) Tantangan yang Dihadapi dan Solusi

Selama pengembangan skrip otomatisasi ini, beberapa tantangan dihadapi, antara lain:

- **Interaksi dengan `AutoCompleteTextView` (Dropdown):** Awalnya, `PopupWindow` yang berisi item dropdown tidak terdeteksi oleh `driver.getPageSource()` atau locator standar. Solusinya adalah menggunakan strategi `sendKeys()` untuk mengetikkan pilihan ke `AutoCompleteTextView`, yang kemudian diverifikasi apakah teks field terupdate, atau (jika `sendKeys` hanya memfilter) menggunakan `AppiumBy.androidUIAutomator` untuk mengklik item yang muncul di popup.
- **Interaksi dengan `DatePicker` dan `TimePicker` Material Design:** ID tombol "OK" pada dialog Material Design bisa berbeda dari ID standar Android. Perlu inspeksi manual dengan Appium Inspector saat dialog aktif untuk menemukan ID yang benar (seringkali `com.google.android.material:id/confirm_button` atau `android:id/button1` untuk versi standar).
- **Timing dan Sinkronisasi:** Penggunaan `WebDriverWait` dengan `ExpectedConditions` yang tepat sangat krusial untuk menunggu elemen muncul dan siap diinteraksikan, terutama setelah navigasi halaman atau saat elemen dimuat secara dinamis. `Thread.sleep()` dihindari sebisa mungkin dan hanya digunakan untuk jeda singkat saat debugging atau saat menunggu pembaruan UI yang sulit ditunggu secara eksplisit.
- **Konfigurasi Environment Appium:** Memastikan semua dependensi (Node, JDK, Android SDK, Appium Server, driver) terinstal dan variabel lingkungan (JAVA_HOME, ANDROID_HOME) terkonfigurasi dengan benar di macOS memerlukan perhatian detail. `appium-doctor` sangat membantu dalam diagnosis.

---

```

**Beberapa Hal yang Perlu Anda Sesuaikan dan Tambahkan:**

* **URL Repositori GitHub Anda:** Ganti `[URL_REPO_GITHUB_ANDA_UNTUK_PROYEK_TES_INI]` dan `[NAMA_FOLDER_PROYEK_TES_ANDA]` dengan informasi yang benar setelah Anda membuat repositori dan mengunggah proyek.
* **Path ke APK di Repositori:** Ganti `[PATH_KE_APK_ANDA_DI_REPO_GITHUB_INI]` dengan path aktual tempat Anda akan meletakkan file `app-debug.apk` di repositori GitHub proyek tes ini (misalnya, buat folder `apk/` di repo tes dan letakkan di sana).
* **Path Absolut APK di Skrip Tes:** Ingatkan diri Anda (dan mungkin di README) bahwa pengguna lain yang menjalankan tes ini perlu mengubah path absolut ke `app-debug.apk` di dalam semua file `.java` (di metode `setUp()`). Cara yang lebih baik untuk proyek bersama adalah menggunakan path relatif atau properti sistem, tapi untuk UAS, path absolut yang didokumentasikan sudah cukup.
* **Logo BengkelGo**: Saya menggunakan link ke logo dari repositori aplikasi BengkelGo Anda. Pastikan link tersebut valid atau unggah logo ke repositori tes Anda dan perbarui linknya.
* **Kredensial Login**: Di bagian `performLogin()` dalam kode dan di README, selalu ingatkan untuk mengganti kredensial contoh dengan yang valid.

Dengan `README.md` ini, proyek Anda akan terdokumentasi dengan baik. Selanjutnya adalah mengoptimalkan kode Java Anda dengan membuat kelas `BaseTest.java`. Apakah Anda siap untuk itu sekarang, atau ada hal lain yang ingin disesuaikan pada `README.md` ini?
```
