package com.uas.kpl;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert; // Import Assert dari TestNG
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Kelas LoginTests berisi skrip tes otomatis untuk fungsionalitas login
 * pada aplikasi BengkelGo.
 */
public class LoginTests {

    private AppiumDriver driver;
    private WebDriverWait wait;

    // Konstanta untuk locator elemen UI agar mudah dikelola
    // Halaman Login
    private static final By EMAIL_FIELD = By.id("com.example.bengkelgo:id/edtEmail");
    private static final By PASSWORD_FIELD = By.id("com.example.bengkelgo:id/edtPassword");
    private static final By LOGIN_BUTTON = By.id("com.example.bengkelgo:id/btnLogin");

    // Halaman Utama (MainActivity) - untuk verifikasi setelah login
    // Pastikan ID ini benar sesuai dengan elemen di MainActivity Anda yang
    // menampilkan nama user
    private static final By USERNAME_TEXT_VIEW_MAIN = By.id("com.example.bengkelgo:id/tvUsername");

    /**
     * Metode setUp dijalankan sebelum setiap metode tes.
     * Metode ini menginisialisasi Appium driver dengan Desired Capabilities
     * yang diperlukan untuk terhubung ke emulator dan aplikasi BengkelGo.
     * 
     * @throws MalformedURLException jika URL Appium server tidak valid.
     */
    @BeforeMethod
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();

        // Konfigurasi Desired Capabilities
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:platformVersion", "15"); // Sesuaikan dengan versi Android emulator/perangkat
        caps.setCapability("appium:udid", "emulator-5554"); // Gunakan UDID emulator atau perangkat fisik
        caps.setCapability("appium:automationName", "UiAutomator2");
        // Path absolut ke file .apk aplikasi BengkelGo
        caps.setCapability("appium:app",
                "/Users/muhammadkhalidalghifari/AndroidStudioProjects/BengkelGo/app/build/outputs/apk/debug/app-debug.apk");

        // (Opsional) Capability untuk mencegah aplikasi di-reset setiap sesi tes
        // caps.setCapability("appium:noReset", true);
        // (Opsional) Capability untuk tidak menghentikan aplikasi setelah tes (berguna
        // saat debugging skrip)
        // caps.setCapability("appium:dontStopAppOnReset", true);

        // Inisialisasi AndroidDriver yang terhubung ke Appium Server
        // Pastikan Appium server berjalan di http://127.0.0.1:4723
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
        // Inisialisasi WebDriverWait dengan durasi timeout default untuk tes ini
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Timeout 20 detik untuk menunggu elemen

        System.out.println("Appium driver initialized and WebDriverWait configured.");
    }

    /**
     * Metode tes untuk skenario login pengguna yang sukses.
     * Langkah-langkah:
     * 1. Tunggu halaman login (field email) muncul.
     * 2. Masukkan email yang valid.
     * 3. Masukkan password yang valid.
     * 4. Klik tombol login.
     * 5. Verifikasi bahwa login berhasil dengan memeriksa elemen di halaman utama.
     */
    @Test(description = "Test Case 1: Verifikasi fungsionalitas login pengguna dengan kredensial yang valid.")
    public void testSuccessfulLogin() {
        System.out.println("Memulai tes: testSuccessfulLogin...");

        // Langkah 1: Tunggu field Email muncul, lalu temukan dan masukkan email
        System.out.println("Mencari field email...");
        WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_FIELD));
        // GANTI "user@gmail.com" dengan email pengguna yang valid untuk tes Anda
        emailField.sendKeys("user@gmail.com");
        System.out.println("Email dimasukkan: user@gmail.com");

        // Langkah 2: Temukan elemen field Password dan masukkan password
        System.out.println("Mencari field password...");
        WebElement passwordField = driver.findElement(PASSWORD_FIELD); // Biasanya sudah visible jika emailField visible
        // GANTI "user123" dengan password yang valid untuk tes Anda
        passwordField.sendKeys("user123");
        System.out.println("Password dimasukkan.");

        // Langkah 3: Temukan elemen tombol Login dan klik
        System.out.println("Mencari tombol login...");
        WebElement loginButton = driver.findElement(LOGIN_BUTTON);
        loginButton.click();
        System.out.println("Tombol login diklik.");

        // Langkah 4: Verifikasi login berhasil
        // Tunggu elemen TextView nama pengguna di MainActivity muncul dan periksa
        // teksnya
        System.out.println("Memverifikasi hasil login...");
        try {
            WebElement usernameTextView = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(USERNAME_TEXT_VIEW_MAIN));
            String actualUsername = usernameTextView.getText();
            // GANTI "user" dengan nama pengguna atau bagian dari email yang diharapkan
            // muncul
            String expectedUsernameOrEmailPart = "user";

            Assert.assertTrue(actualUsername.contains(expectedUsernameOrEmailPart),
                    "Nama pengguna yang ditampilkan setelah login tidak sesuai. Aktual: " + actualUsername +
                            ", Diharapkan mengandung: " + expectedUsernameOrEmailPart);
            System.out.println("Login berhasil! Nama pengguna yang ditampilkan: " + actualUsername);

        } catch (Exception e) {
            System.err.println(
                    "Verifikasi login gagal: Elemen nama pengguna di halaman utama tidak ditemukan atau terjadi error lain.");
            e.printStackTrace(); // Cetak stack trace untuk debugging
            Assert.fail("Login gagal diverifikasi. Elemen " + USERNAME_TEXT_VIEW_MAIN.toString()
                    + " tidak ditemukan atau error: " + e.getMessage());
        }

        System.out.println("Tes testSuccessfulLogin selesai.");
    }

    /**
     * Metode tearDown dijalankan setelah setiap metode tes.
     * Metode ini menghentikan sesi Appium driver.
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Sesi Appium driver dihentikan.");
        }
    }
}