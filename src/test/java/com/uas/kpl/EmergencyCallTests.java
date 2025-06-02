package com.uas.kpl;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
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
 * Kelas EmergencyCallTests berisi skrip tes otomatis untuk fungsionalitas
 * panggilan darurat pada aplikasi BengkelGo.
 */
public class EmergencyCallTests {

    private AndroidDriver driver;
    private WebDriverWait wait;

    // --- Konstanta Locator ---
    // MainActivity
    private static final By EMERGENCY_CALL_CARD = By.id("com.example.bengkelgo:id/cardEmergencyCall");
    private static final By USERNAME_TEXT_VIEW_MAIN_FOR_LOGIN_CHECK = By.id("com.example.bengkelgo:id/tvUsername");

    // Dialog Emergency Call
    private static final By DIALOG_TITLE = By.id("com.example.bengkelgo:id/tvDialogTitle");
    private static final By DIALOG_DESCRIPTION = By.id("com.example.bengkelgo:id/tvEmergencyDescription");
    private static final By DIALOG_NUMBER_LABEL = By.id("com.example.bengkelgo:id/tvEmergencyNumberLabel");
    private static final By DIALOG_NUMBER = By.id("com.example.bengkelgo:id/tvEmergencyNumber");
    private static final By DIALOG_CONFIRM_CALL_BUTTON = By.id("com.example.bengkelgo:id/btnConfirmEmergencyCall");
    private static final By DIALOG_CANCEL_BUTTON = By.id("com.example.bengkelgo:id/btnCancelEmergencyCall");

    // Package name untuk aplikasi Dialer (bisa berbeda antar perangkat/OS)
    private static final String DIALER_PACKAGE_GOOGLE = "com.google.android.dialer"; // Untuk Pixel/Stock Android
    private static final String DIALER_PACKAGE_SAMSUNG = "com.samsung.android.dialer"; // Contoh untuk Samsung
    private static final String DIALER_PACKAGE_ANDROID_DEFAULT = "com.android.dialer"; // Default generik

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:platformVersion", "15"); // Sesuaikan
        caps.setCapability("appium:udid", "emulator-5554"); // Sesuaikan
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:app",
                "/Users/muhammadkhalidalghifari/AndroidStudioProjects/BengkelGo/app/build/outputs/apk/debug/app-debug.apk");
        caps.setCapability("appium:noReset", true); // Asumsikan sudah login

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        System.out.println("Appium driver initialized for EmergencyCallTests.");

        // Cek dan lakukan login jika perlu
        try {
            System.out.println("Mengecek status login...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_TEXT_VIEW_MAIN_FOR_LOGIN_CHECK));
            System.out.println("Pengguna sudah login, berada di MainActivity.");
        } catch (Exception e) {
            System.out.println("Pengguna belum login atau tidak di MainActivity. Mencoba login...");
            performLogin("user@gmail.com", "user123"); // Ganti dengan kredensial valid
            wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_TEXT_VIEW_MAIN_FOR_LOGIN_CHECK));
            System.out.println("Login berhasil, berada di MainActivity.");
        }
    }

    private void performLogin(String email, String password) {
        WebDriverWait loginWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement emailField = loginWait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("com.example.bengkelgo:id/edtEmail")));
        emailField.sendKeys(email);
        driver.findElement(By.id("com.example.bengkelgo:id/edtPassword")).sendKeys(password);
        driver.findElement(By.id("com.example.bengkelgo:id/btnLogin")).click();
    }

    @Test(description = "Test Case: Memverifikasi tampilan dialog panggilan darurat dan aksi tombol 'Hubungi Sekarang'.")
    public void testEmergencyCallDialogAndAction() throws InterruptedException {
        System.out.println("Memulai tes: testEmergencyCallDialogAndAction...");

        // 1. Dari MainActivity, klik card "Emergency Call"
        System.out.println("Mengklik card Emergency Call...");
        WebElement emergencyCallCard = wait.until(ExpectedConditions.elementToBeClickable(EMERGENCY_CALL_CARD));
        emergencyCallCard.click();
        System.out.println("Card Emergency Call diklik.");

        // 2. Verifikasi elemen-elemen di dialog panggilan darurat
        System.out.println("Memverifikasi dialog panggilan darurat...");
        WebElement dialogTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(DIALOG_TITLE));
        Assert.assertTrue(dialogTitle.isDisplayed(), "Judul dialog tidak muncul.");
        Assert.assertEquals(dialogTitle.getText(), "Panggilan Darurat", "Teks judul dialog tidak sesuai."); // Ambil
                                                                                                            // dari
                                                                                                            // R.string.emergency_call_dialog_title
        System.out.println("Judul dialog terverifikasi.");

        WebElement dialogDescription = driver.findElement(DIALOG_DESCRIPTION);
        Assert.assertTrue(dialogDescription.isDisplayed(), "Deskripsi dialog tidak muncul.");
        // Anda bisa menambahkan assert untuk teks deskripsi jika perlu
        System.out.println("Deskripsi dialog terverifikasi.");

        WebElement dialogNumber = driver.findElement(DIALOG_NUMBER);
        Assert.assertTrue(dialogNumber.isDisplayed(), "Nomor darurat tidak muncul.");
        Assert.assertEquals(dialogNumber.getText(), "0812-0000-0000", "Nomor darurat tidak sesuai."); // Ambil dari
                                                                                                      // R.string.emergency_call_dummy_number
        System.out.println("Nomor darurat terverifikasi.");

        WebElement confirmCallButton = driver.findElement(DIALOG_CONFIRM_CALL_BUTTON);
        Assert.assertTrue(confirmCallButton.isDisplayed() && confirmCallButton.isEnabled(),
                "Tombol 'Hubungi Sekarang' tidak muncul atau tidak aktif.");
        System.out.println("Tombol 'Hubungi Sekarang' terverifikasi.");

        // 3. Klik tombol "Hubungi Sekarang"
        confirmCallButton.click();
        System.out.println("Tombol 'Hubungi Sekarang' diklik.");

        // 4. Verifikasi bahwa intent ke Dialer dipicu (pendekatan: cek current
        // activity)
        // Ini mungkin memerlukan jeda agar activity sempat berganti.
        Thread.sleep(3000); // Tunggu 3 detik agar Dialer sempat terbuka

        String currentActivity = driver.currentActivity();
        String currentPackage = driver.getCurrentPackage(); // Atau getPackage()
        System.out.println("Activity saat ini setelah klik Hubungi: " + currentActivity);
        System.out.println("Package saat ini: " + currentPackage);

        // Verifikasi apakah package saat ini adalah salah satu package Dialer yang
        // dikenal
        boolean isDialerOpened = DIALER_PACKAGE_GOOGLE.equals(currentPackage) ||
                DIALER_PACKAGE_SAMSUNG.equals(currentPackage) ||
                DIALER_PACKAGE_ANDROID_DEFAULT.equals(currentPackage);

        Assert.assertTrue(isDialerOpened,
                "Aplikasi Dialer tidak terbuka setelah mengklik 'Hubungi Sekarang'. Package saat ini: "
                        + currentPackage);
        System.out.println("Verifikasi: Aplikasi Dialer terbuka (package: " + currentPackage + ").");

        // Untuk kembali ke aplikasi Anda setelah Dialer terbuka (jika diperlukan untuk
        // tes selanjutnya,
        // atau jika Anda ingin menutup Dialer dan melanjutkan di aplikasi Anda).
        // Ini mungkin tidak selalu berhasil dengan mudah tergantung bagaimana Dialer
        // menangani back press.
        // driver.navigate().back(); // Coba tekan tombol back
        // Thread.sleep(1000);
        // Anda mungkin perlu mengaktifkan kembali aplikasi BengkelGo secara eksplisit
        // jika `back()` tidak kembali ke sana.
        // driver.activateApp("com.example.bengkelgo");

        System.out.println("Tes testEmergencyCallDialogAndAction selesai.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            // Jika kita ingin meninggalkan aplikasi Dialer terbuka untuk verifikasi manual,
            // kita mungkin tidak ingin `driver.quit()` langsung di sini, atau kita perlu
            // strategi untuk kembali ke aplikasi kita sebelum quit.
            // Untuk tes otomatis penuh, quit tetap penting.
            // Jika Anda ingin melihat Dialer tetap terbuka setelah tes, komentari quit()
            // sementara.
            driver.quit();
            System.out.println("Sesi Appium driver dihentikan untuk EmergencyCallTests.");
        }
    }
}