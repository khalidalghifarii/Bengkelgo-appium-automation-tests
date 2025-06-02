package com.uas.kpl;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

/**
 * Kelas RegistrationTests berisi skrip tes otomatis untuk fungsionalitas
 * registrasi
 * pengguna baru pada aplikasi BengkelGo.
 */
public class RegistrationTests {

    private AppiumDriver driver;
    private WebDriverWait wait;

    // --- Konstanta untuk Locator Elemen UI ---

    // Halaman Login (untuk link Sign Up)
    private static final By LOGIN_PAGE_EMAIL_FIELD = By.id("com.example.bengkelgo:id/edtEmail"); // Untuk memastikan
                                                                                                 // kita di halaman
                                                                                                 // login
    private static final By SIGN_UP_LINK = By.id("com.example.bengkelgo:id/tvSignUp");

    // Halaman Registrasi
    private static final By NAME_FIELD_REG = By.id("com.example.bengkelgo:id/edtName");
    private static final By EMAIL_FIELD_REG = By.id("com.example.bengkelgo:id/edtEmail"); // ID bisa sama jika di layout
                                                                                          // berbeda
    private static final By PHONE_FIELD_REG = By.id("com.example.bengkelgo:id/edtPhone");
    private static final By ADDRESS_FIELD_REG = By.id("com.example.bengkelgo:id/edtAddress");
    private static final By PASSWORD_FIELD_REG = By.id("com.example.bengkelgo:id/edtPassword"); // ID bisa sama
    private static final By CONTINUE_BUTTON_REG = By.id("com.example.bengkelgo:id/btnContinue");

    // Halaman Congratulations
    private static final By CONGRATULATIONS_TITLE = By.id("com.example.bengkelgo:id/tvCongratulations");
    private static final By SUCCESS_MESSAGE_TEXT_VIEW = By.id("com.example.bengkelgo:id/tvMessage");
    private static final By LOGIN_NOW_BUTTON_CONGRATS = By.id("com.example.bengkelgo:id/btnLoginPage");

    /**
     * Metode setUp dijalankan sebelum setiap metode tes dalam kelas ini.
     * Menginisialisasi Appium driver dan WebDriverWait.
     * 
     * @throws MalformedURLException jika URL Appium server tidak valid.
     */
    @BeforeMethod
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:platformVersion", "15"); // Sesuaikan dengan emulator Anda
        caps.setCapability("appium:udid", "emulator-5554"); // Sesuaikan dengan emulator Anda
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:app",
                "/Users/muhammadkhalidalghifari/AndroidStudioProjects/BengkelGo/app/build/outputs/apk/debug/app-debug.apk"); // Path
                                                                                                                             // ke
                                                                                                                             // APK
                                                                                                                             // Anda

        // Opsi untuk memastikan kondisi aplikasi bersih untuk setiap tes registrasi
        // 'fullReset' akan meng-uninstall dan meng-install ulang aplikasi.
        // 'noReset=false' (default) akan membersihkan data aplikasi.
        // caps.setCapability("appium:fullReset", true); // Gunakan jika perlu kondisi
        // super bersih
        // caps.setCapability("appium:noReset", false); // Default, membersihkan data
        // aplikasi

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Timeout default 20 detik

        System.out.println("Appium driver initialized for RegistrationTests.");
    }

    /**
     * DataProvider untuk menghasilkan email unik.
     * Ini membantu agar tes registrasi bisa dijalankan berulang kali tanpa error
     * email sudah terdaftar.
     * 
     * @return Object array berisi email unik.
     */
    @DataProvider(name = "uniqueEmailProvider")
    public Object[][] uniqueEmailProvider() {
        String uniqueEmail = "testuser" + System.currentTimeMillis() + "@example.com";
        return new Object[][] { { uniqueEmail } };
    }

    /**
     * Metode tes untuk skenario registrasi pengguna baru yang sukses.
     * Menggunakan DataProvider untuk mendapatkan email yang unik setiap kali tes
     * dijalankan.
     * 
     * @param uniqueEmail Email unik yang akan digunakan untuk registrasi.
     */
    @Test(description = "Test Case 2: Verifikasi fungsionalitas registrasi pengguna baru dengan data yang valid.", dataProvider = "uniqueEmailProvider")
    public void testSuccessfulRegistration(String uniqueEmail) {
        System.out.println("Memulai tes: testSuccessfulRegistration dengan email: " + uniqueEmail + "...");

        // Langkah 1: Pastikan berada di halaman Login dan klik link "Sign Up"
        // Menunggu field email di halaman login muncul sebagai indikasi halaman login
        // sudah termuat
        System.out.println("Menunggu halaman Login muncul...");
        wait.until(ExpectedConditions.visibilityOfElementLocated(LOGIN_PAGE_EMAIL_FIELD));

        System.out.println("Mencari dan mengklik link Sign Up...");
        WebElement signUpLink = wait.until(ExpectedConditions.elementToBeClickable(SIGN_UP_LINK));
        signUpLink.click();
        System.out.println("Link Sign Up diklik. Pindah ke halaman Registrasi.");

        // Langkah 2: Isi form registrasi di halaman Registrasi
        System.out.println("Mengisi form registrasi...");
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(NAME_FIELD_REG));
        nameField.sendKeys("Test User UAS Registrasi");
        System.out.println("Nama dimasukkan: Test User UAS Registrasi");

        // ID EMAIL_FIELD_REG mungkin sama dengan di halaman Login,
        // pastikan ini adalah elemen yang benar di halaman Registrasi.
        driver.findElement(EMAIL_FIELD_REG).sendKeys(uniqueEmail);
        System.out.println("Email (unik) dimasukkan: " + uniqueEmail);

        driver.findElement(PHONE_FIELD_REG).sendKeys("089876543210");
        System.out.println("No Hp dimasukkan: 089876543210");

        driver.findElement(ADDRESS_FIELD_REG).sendKeys("Jl. Registrasi No. 789, Kota Tes");
        System.out.println("Alamat dimasukkan: Jl. Registrasi No. 789, Kota Tes");

        // ID PASSWORD_FIELD_REG mungkin sama dengan di halaman Login
        driver.findElement(PASSWORD_FIELD_REG).sendKeys("regpass123");
        System.out.println("Password registrasi dimasukkan: regpass123");

        // Tombol "Continue"
        // Mungkin perlu scroll jika tombol tidak terlihat, tapi kita coba langsung
        // dulu.
        WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(CONTINUE_BUTTON_REG));
        continueButton.click();
        System.out.println("Tombol Continue diklik.");

        // Langkah 3: Verifikasi halaman Congratulations
        System.out.println("Memverifikasi halaman Congratulations...");

        WebElement congratsTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(CONGRATULATIONS_TITLE));
        Assert.assertTrue(congratsTitle.isDisplayed(), "Judul 'Congratulations!' tidak muncul di halaman konfirmasi.");
        Assert.assertEquals(congratsTitle.getText(), "Congratulations!",
                "Teks judul di halaman konfirmasi tidak sesuai.");
        System.out.println("Judul 'Congratulations!' terverifikasi.");

        WebElement successMessage = driver.findElement(SUCCESS_MESSAGE_TEXT_VIEW);
        // Verifikasi bahwa pesan mengandung teks yang diharapkan.
        // Anda bisa mengambil string R.string.register_success dari aplikasi Android
        // Anda untuk perbandingan yang lebih akurat jika perlu.
        String expectedMessagePart = "You have successfully created an account";
        Assert.assertTrue(successMessage.getText().contains(expectedMessagePart),
                "Pesan sukses registrasi tidak sesuai. Aktual: '" + successMessage.getText() +
                        "', Diharapkan mengandung: '" + expectedMessagePart + "'");
        System.out.println("Pesan sukses registrasi terverifikasi: " + successMessage.getText());

        WebElement loginNowButton = driver.findElement(LOGIN_NOW_BUTTON_CONGRATS);
        Assert.assertTrue(loginNowButton.isDisplayed() && loginNowButton.isEnabled(),
                "Tombol 'Login Now' tidak muncul atau tidak aktif di halaman konfirmasi.");
        System.out.println("Tombol 'Login Now' terverifikasi.");

        System.out.println("Tes testSuccessfulRegistration selesai dengan sukses.");
    }

    /**
     * Metode tearDown dijalankan setelah setiap metode tes dalam kelas ini.
     * Menghentikan sesi Appium driver.
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Sesi Appium driver dihentikan untuk RegistrationTests.");
        }
    }
}