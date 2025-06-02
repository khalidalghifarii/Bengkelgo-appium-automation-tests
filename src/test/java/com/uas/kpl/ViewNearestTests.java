package com.uas.kpl; // Sesuaikan dengan package Anda

import io.appium.java_client.android.AndroidDriver; // Pastikan import ini benar
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Kelas ViewNearestTests berisi skrip tes otomatis untuk fungsionalitas
 * melihat layanan terdekat pada aplikasi BengkelGo.
 */
public class ViewNearestTests {

    private AndroidDriver driver; // Menggunakan AndroidDriver secara eksplisit
    private WebDriverWait wait;

    // --- Konstanta Locator ---
    // MainActivity
    private static final By VIEW_NEAREST_CARD = By.id("com.example.bengkelgo:id/cardViewNearest");
    private static final By USERNAME_TEXT_VIEW_MAIN_FOR_LOGIN_CHECK = By.id("com.example.bengkelgo:id/tvUsername");
    // Untuk verifikasi kembali ke MainActivity, bisa gunakan salah satu card utama
    private static final By SPARE_PART_SHOP_CARD_MAIN = By.id("com.example.bengkelgo:id/cardSparePartShop");

    // NearestServiceActivity
    private static final By BACK_BUTTON_NEAREST_SERVICE = By.id("com.example.bengkelgo:id/btnBackNearestService");
    private static final By TITLE_NEAREST_SERVICE = By.id("com.example.bengkelgo:id/tvTitleNearestService");
    // Alternatif untuk verifikasi halaman NearestServiceActivity termuat:
    // private static final By MAP_PLACEHOLDER_NEAREST_SERVICE =
    // By.id("com.example.bengkelgo:id/imgMapPlaceholder");

    /**
     * Metode setUp dijalankan sebelum setiap metode tes dalam kelas ini.
     * Menginisialisasi Appium driver dan WebDriverWait, serta menangani login.
     * 
     * @throws MalformedURLException jika URL Appium server tidak valid.
     */
    @BeforeMethod
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:platformVersion", "15"); // Sesuaikan dengan versi Android emulator Anda
        caps.setCapability("appium:udid", "emulator-5554"); // Sesuaikan dengan UDID emulator Anda
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:app",
                "/Users/muhammadkhalidalghifari/AndroidStudioProjects/BengkelGo/app/build/outputs/apk/debug/app-debug.apk"); // Path
                                                                                                                             // ke
                                                                                                                             // APK
                                                                                                                             // Anda

        // Menggunakan noReset=true agar tidak perlu login ulang jika sesi sebelumnya
        // sudah login.
        // Jika ingin kondisi bersih setiap tes, set ke false atau hapus (defaultnya
        // false),
        // lalu pastikan performLogin() selalu dipanggil dan berhasil.
        caps.setCapability("appium:noReset", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Timeout default 20 detik

        System.out.println("Appium driver initialized for ViewNearestTests.");

        // Memastikan pengguna sudah login sebelum menjalankan tes
        try {
            System.out.println("Mengecek status login...");
            // Tunggu elemen di MainActivity yang menandakan sudah login
            wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_TEXT_VIEW_MAIN_FOR_LOGIN_CHECK));
            System.out.println("Pengguna sudah login, berada di MainActivity.");
        } catch (Exception e) {
            System.out.println("Pengguna belum login atau tidak di MainActivity. Mencoba login...");
            performLogin("user@gmail.com", "user123"); // GANTI DENGAN KREDENSIAL VALID ANDA
            // Verifikasi lagi setelah login
            wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_TEXT_VIEW_MAIN_FOR_LOGIN_CHECK));
            System.out.println("Login berhasil, berada di MainActivity.");
        }
    }

    /**
     * Metode helper untuk melakukan proses login.
     * 
     * @param email    Email pengguna.
     * @param password Password pengguna.
     */
    private void performLogin(String email, String password) {
        // Asumsi aplikasi akan berada di halaman login jika belum ada sesi aktif
        WebDriverWait loginWait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Wait khusus untuk login

        WebElement emailField = loginWait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("com.example.bengkelgo:id/edtEmail")));
        emailField.sendKeys(email);

        WebElement passwordField = driver.findElement(By.id("com.example.bengkelgo:id/edtPassword"));
        passwordField.sendKeys(password);

        WebElement loginButton = driver.findElement(By.id("com.example.bengkelgo:id/btnLogin"));
        loginButton.click();
    }

    /**
     * Metode tes untuk skenario membuka halaman "View Nearest" dan kembali ke
     * halaman utama.
     * Langkah-langkah:
     * 1. Dari MainActivity, klik card "View Nearest".
     * 2. Verifikasi halaman "Nearest Service" muncul.
     * 3. Tunggu beberapa saat.
     * 4. Klik tombol "Back" di halaman "Nearest Service".
     * 5. Verifikasi kembali ke MainActivity.
     * 
     * @throws InterruptedException jika Thread.sleep diinterupsi.
     */
    @Test(description = "Test Case: Membuka halaman 'View Nearest' dan kembali ke halaman utama.")
    public void testOpenViewNearestAndGoBack() throws InterruptedException {
        System.out.println("Memulai tes: testOpenViewNearestAndGoBack...");

        // 1. Dari MainActivity, klik card "View Nearest"
        System.out.println("Mengklik card View Nearest...");
        WebElement viewNearestCard = wait.until(ExpectedConditions.elementToBeClickable(VIEW_NEAREST_CARD));
        viewNearestCard.click();
        System.out.println("Card View Nearest diklik. Menunggu NearestServiceActivity...");

        // 2. Di NearestServiceActivity
        // Verifikasi bahwa halaman "Nearest Service" muncul dengan mengecek judulnya
        WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE_NEAREST_SERVICE));
        Assert.assertTrue(pageTitle.isDisplayed(), "Judul halaman Nearest Service tidak muncul.");
        // Anda bisa mengganti teks "Nearest Service" dengan string resource jika ada
        // dan berbeda
        Assert.assertEquals(pageTitle.getText(), "Nearest Service", "Teks judul halaman Nearest Service tidak sesuai.");
        System.out.println("Halaman Nearest Service termuat dan judul terverifikasi: " + pageTitle.getText());

        // Tambahkan jeda singkat untuk simulasi pengguna melihat halaman
        System.out.println("Menunggu 3 detik di halaman Nearest Service...");
        Thread.sleep(3000); // Jeda 3 detik

        // Klik tombol "Back" di halaman "Nearest Service"
        System.out.println("Mengklik tombol Back di Nearest Service...");
        WebElement backButton = wait.until(ExpectedConditions.elementToBeClickable(BACK_BUTTON_NEAREST_SERVICE));
        backButton.click();
        System.out.println("Tombol Back diklik. Menunggu kembali ke MainActivity...");

        // 3. Kembali di MainActivity
        // Verifikasi bahwa aplikasi telah kembali ke MainActivity
        // dengan mengecek keberadaan salah satu card layanan, misalnya Spare Part Shop
        // card.
        WebElement sparePartCardMain = wait
                .until(ExpectedConditions.visibilityOfElementLocated(SPARE_PART_SHOP_CARD_MAIN));
        Assert.assertTrue(sparePartCardMain.isDisplayed(),
                "Gagal kembali ke MainActivity atau elemen MainActivity (Spare Part Card) tidak ditemukan.");
        System.out.println("Berhasil kembali ke MainActivity (Spare Part Card terlihat).");

        System.out.println("Tes testOpenViewNearestAndGoBack selesai.");
    }

    /**
     * Metode tearDown dijalankan setelah setiap metode tes.
     * Menghentikan sesi Appium driver.
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Sesi Appium driver dihentikan untuk ViewNearestTests.");
        }
    }
}