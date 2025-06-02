package com.uas.kpl; // Sesuaikan package

import io.appium.java_client.AppiumBy; // Pastikan ini ada
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
// Hapus import java.util.Set; jika tidak lagi digunakan untuk getContextHandles
// import java.util.Set; 

/**
 * Kelas VehicleServiceOrderTests berisi skrip tes otomatis untuk fungsionalitas
 * pemesanan servis kendaraan pada aplikasi BengkelGo.
 */
public class VehicleServiceOrderTests {

    private AndroidDriver driver;
    private WebDriverWait wait;

    // --- Konstanta Locator ---
    // MainActivity
    private static final By VEHICLE_REPAIR_CARD = By.id("com.example.bengkelgo:id/cardVehicleRepair");
    private static final By USERNAME_TEXT_VIEW_MAIN_FOR_LOGIN_CHECK = By.id("com.example.bengkelgo:id/tvUsername");

    // VehicleRepairFormActivity
    private static final By VEHICLE_TYPE_DROPDOWN = By.id("com.example.bengkelgo:id/actVehicleType");
    private static final By SERVICE_TYPE_DROPDOWN = By.id("com.example.bengkelgo:id/actServiceType");
    private static final By LOCATION_DROPDOWN = By.id("com.example.bengkelgo:id/actLocation");
    private static final By SERVICE_SCHEDULE_FIELD = By.id("com.example.bengkelgo:id/edtServiceSchedule");
    private static final By PAYMENT_METHOD_DROPDOWN = By.id("com.example.bengkelgo:id/actPaymentMethod");
    private static final By NEXT_BUTTON_FORM = By.id("com.example.bengkelgo:id/btnNext");
    // ID Tombol OK untuk DatePicker dan TimePicker standar Android
    private static final By DIALOG_OK_BUTTON = By.id("android:id/button1");
    private static final By MATERIAL_DIALOG_OK_BUTTON = By.id("com.google.android.material:id/confirm_button");

    // OrderSummaryActivity
    private static final By COMPLETE_ADDRESS_FIELD = By.id("com.example.bengkelgo:id/edtCompleteAddress");
    private static final By ORDER_NOW_BUTTON_SUMMARY = By.id("com.example.bengkelgo:id/btnOrderNow");

    // OrderConfirmationActivity
    // PASTIKAN ID INI BENAR DARI activity_order_confirmation.xml (misalnya dari
    // layout)
    private static final By ORDER_RECEIVED_TITLE_TEXT_CONFIRM = By.id("com.example.bengkelgo:id/tvOrderReceivedTitle");
    private static final By BACK_TO_HOME_BUTTON_CONFIRMATION = By.id("com.example.bengkelgo:id/btnBackToHome");

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("platformName", "Android");
        caps.setCapability("appium:platformVersion", "15");
        caps.setCapability("appium:udid", "emulator-5554");
        caps.setCapability("appium:automationName", "UiAutomator2");
        caps.setCapability("appium:app",
                "/Users/muhammadkhalidalghifari/AndroidStudioProjects/BengkelGo/app/build/outputs/apk/debug/app-debug.apk");
        caps.setCapability("appium:noReset", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), caps);
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        System.out.println("Appium driver initialized for VehicleServiceOrderTests.");

        try {
            System.out.println("Mengecek status login...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_TEXT_VIEW_MAIN_FOR_LOGIN_CHECK));
            System.out.println("Pengguna sudah login, berada di MainActivity.");
        } catch (Exception e) {
            System.out.println("Pengguna belum login atau tidak di MainActivity. Mencoba login...");
            performLogin("user@gmail.com", "user123"); // GANTI DENGAN KREDENSIAL VALID
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

    /**
     * Metode helper untuk memilih item dari AutoCompleteTextView dengan strategi
     * sendKeys
     * dan kemudian mengklik item yang muncul di popup jika teks belum terupdate.
     * 
     * @param dropdownElement WebElement AutoCompleteTextView
     * @param itemText        Teks item yang ingin dipilih
     * @param itemNameForLog  Nama dropdown untuk logging (e.g., "Jenis Kendaraan")
     * @throws InterruptedException jika Thread.sleep diinterupsi
     */
    private void selectDropdownItemWithSendKeys(WebElement dropdownElement, String itemText, String itemNameForLog)
            throws InterruptedException {
        System.out.println("Mencoba mengisi " + itemNameForLog + " dengan sendKeys: '" + itemText + "'");
        dropdownElement.click(); // Klik dulu untuk memunculkan daftar/fokus
        Thread.sleep(500); // Jeda singkat agar siap menerima input
        dropdownElement.sendKeys(itemText);
        System.out.println("Teks '" + itemText + "' dikirim ke dropdown " + itemNameForLog + ".");
        Thread.sleep(1000); // Beri waktu untuk AutoCompleteTextView memproses & mungkin memfilter

        String currentTextInDropdown = dropdownElement.getText();
        System.out.println("Teks di dropdown " + itemNameForLog + " setelah sendKeys: " + currentTextInDropdown);

        if (itemText.equals(currentTextInDropdown)) {
            System.out.println(
                    itemNameForLog + " '" + itemText + "' berhasil diisi via sendKeys dan teks terkonfirmasi.");
        } else {
            System.out.println("Teks dropdown " + itemNameForLog + " belum '" + itemText
                    + "'. Mencoba mencari dan mengklik '" + itemText + "' dari popup...");
            // Cetak page source untuk debugging jika diperlukan
            // System.err.println("PAGE SOURCE setelah sendKeys('" + itemText + "') ke " +
            // itemNameForLog + ":\n" + driver.getPageSource());

            By itemLocator = AppiumBy.androidUIAutomator(
                    String.format("new UiSelector().text(\"%s\").className(\"android.widget.TextView\")", itemText));
            try {
                WebElement itemOption = wait.until(ExpectedConditions.elementToBeClickable(itemLocator));
                System.out.println(
                        "Item '" + itemText + "' ditemukan di popup untuk " + itemNameForLog + ". Mencoba mengklik...");
                itemOption.click();
                System.out.println("Item '" + itemText + "' di popup untuk " + itemNameForLog + " diklik.");

                Thread.sleep(500);
                currentTextInDropdown = dropdownElement.getText();
                Assert.assertEquals(currentTextInDropdown, itemText,
                        "Teks di dropdown " + itemNameForLog + " tidak terupdate menjadi '" + itemText
                                + "' setelah klik dari popup.");
                System.out.println("Verifikasi berhasil: Dropdown " + itemNameForLog + " menampilkan '"
                        + currentTextInDropdown + "'.");
            } catch (Exception e) {
                System.err.println("GAGAL memilih '" + itemText + "' dari dropdown " + itemNameForLog + ". Error: "
                        + e.getMessage());
                System.err.println("PAGE SOURCE saat error memilih '" + itemText + "' untuk " + itemNameForLog + ":\n"
                        + driver.getPageSource());
                e.printStackTrace();
                Assert.fail("Gagal memilih '" + itemText + "' dari dropdown " + itemNameForLog + ".", e);
            }
        }
    }

    @Test(description = "Test Case 3: Verifikasi alur pemesanan servis kendaraan berhasil.")
    public void testSuccessfulVehicleServiceOrder() throws InterruptedException {
        System.out.println("Memulai tes: testSuccessfulVehicleServiceOrder...");

        // 1. Dari MainActivity, klik card "Vehicle Repair"
        System.out.println("Mengklik card Vehicle Repair...");
        WebElement vehicleRepairCard = wait.until(ExpectedConditions.elementToBeClickable(VEHICLE_REPAIR_CARD));
        vehicleRepairCard.click();
        System.out.println("Card Vehicle Repair diklik. Menunggu VehicleRepairFormActivity...");

        WebElement vehicleTypeDropdownElement = wait
                .until(ExpectedConditions.visibilityOfElementLocated(VEHICLE_TYPE_DROPDOWN));
        System.out.println("VehicleRepairFormActivity termuat.");
        System.out.println("Mengisi form Vehicle Repair...");

        // --- Mengisi Dropdown Jenis Kendaraan ---
        selectDropdownItemWithSendKeys(vehicleTypeDropdownElement, "Mobil", "Jenis Kendaraan");

        // --- Mengisi Dropdown Jenis Servis ---
        Thread.sleep(1500);
        WebElement serviceTypeDropdownElement = driver.findElement(SERVICE_TYPE_DROPDOWN);
        selectDropdownItemWithSendKeys(serviceTypeDropdownElement, "Ganti Oli", "Jenis Servis");

        // --- Mengisi Dropdown Lokasi ---
        Thread.sleep(500);
        WebElement locationDropdownElement = driver.findElement(LOCATION_DROPDOWN);
        selectDropdownItemWithSendKeys(locationDropdownElement, "Banda Aceh", "Lokasi Area");

        // --- Interaksi dengan Jadwal Servis (DatePicker & TimePicker) ---
        Thread.sleep(500);
        System.out.println("Memilih Jadwal Servis...");
        driver.findElement(SERVICE_SCHEDULE_FIELD).click();
        System.out.println("Field Jadwal diklik. Menunggu DatePicker...");

        try {
            // Tunggu tombol OK di DatePicker muncul dan bisa diklik
            System.out.println("Menunggu tombol OK DatePicker...");
            WebElement datePickerOkButton = wait.until(ExpectedConditions.elementToBeClickable(DIALOG_OK_BUTTON));
            System.out.println("Tombol OK DatePicker ditemukan. Mencoba mengklik...");
            datePickerOkButton.click();
            System.out.println("Tombol OK DatePicker diklik (memilih tanggal default/saat ini).");

            // Tunggu tombol OK di TimePicker muncul dan bisa diklik
            // TimePicker muncul setelah DatePicker ditutup.
            System.out.println("Menunggu tombol OK TimePicker...");
            // Perlu dipastikan bahwa ID DIALOG_OK_BUTTON (android:id/button1)
            // juga valid untuk tombol OK di TimePicker Material.
            // Screenshot Anda menunjukkan ini untuk TimePicker, jadi seharusnya benar.
            WebElement timePickerOkButton = wait.until(ExpectedConditions.elementToBeClickable(DIALOG_OK_BUTTON));
            System.out.println("Tombol OK TimePicker ditemukan. Mencoba mengklik...");
            timePickerOkButton.click();
            System.out.println("Tombol OK TimePicker diklik (memilih waktu default/saat ini).");

            // Verifikasi bahwa field jadwal sekarang terisi teks (opsional tapi bagus)
            Thread.sleep(500); // Beri waktu teks di field jadwal terupdate
            String scheduleText = driver.findElement(SERVICE_SCHEDULE_FIELD).getText();
            Assert.assertFalse(scheduleText.isEmpty() || scheduleText.equals("Pilih Jadwal Servis"),
                    "Field Jadwal Servis tidak terisi setelah memilih tanggal dan waktu.");
            System.out.println("Field Jadwal Servis terisi: " + scheduleText);

        } catch (Exception e) {
            System.err.println("GAGAL berinteraksi dengan Date/Time Picker: " + e.getMessage());
            System.err.println("PAGE SOURCE saat error Date/Time Picker:\n" + driver.getPageSource());
            e.printStackTrace();
            Assert.fail(
                    "Gagal berinteraksi dengan Date/Time Picker. Pastikan locator DIALOG_OK_BUTTON benar untuk kedua dialog.",
                    e);
        }

        // --- Mengisi Dropdown Metode Pembayaran ---
        Thread.sleep(500);
        WebElement paymentMethodDropdownElement = driver.findElement(PAYMENT_METHOD_DROPDOWN);
        selectDropdownItemWithSendKeys(paymentMethodDropdownElement, "Cash (Bayar di Tempat)", "Metode Pembayaran");

        // Klik Tombol Next
        System.out.println("Mengklik tombol Next...");
        driver.findElement(NEXT_BUTTON_FORM).click();
        System.out.println("Tombol Next diklik, pindah ke Order Summary.");

        // 3. Isi dan verifikasi di OrderSummaryActivity
        System.out.println("Menunggu dan mengisi Order Summary...");
        WebElement completeAddressField = wait
                .until(ExpectedConditions.visibilityOfElementLocated(COMPLETE_ADDRESS_FIELD));
        completeAddressField.sendKeys("Jl. Tes Otomatis Sukses No. 123, Kota Appium"); // Alamat baru
        System.out.println("Alamat lengkap dimasukkan.");

        // Penting: Tambahkan jeda atau wait di sini jika tombol "Order Now" mungkin
        // belum aktif segera
        wait.until(ExpectedConditions.elementToBeClickable(ORDER_NOW_BUTTON_SUMMARY));
        driver.findElement(ORDER_NOW_BUTTON_SUMMARY).click();
        System.out.println("Tombol Order Now diklik. Menunggu halaman konfirmasi...");

        // 4. Verifikasi di OrderConfirmationActivity
        System.out.println("Memverifikasi halaman Order Confirmation...");

        // Cetak Page Source SEBELUM mencari elemen di halaman konfirmasi
        Thread.sleep(2000); // Beri waktu sedikit untuk transisi halaman
        System.err.println("PAGE SOURCE di Halaman Konfirmasi (Diharapkan):\n" + driver.getPageSource());

        // MODIFIKASI LOCATOR: Menggunakan XPath berdasarkan teks karena tidak ada ID
        // unik untuk judul
        By orderReceivedTitleXPath = By.xpath("//android.widget.TextView[@text='Order Received!']");
        // atau jika ingin lebih spesifik (misalnya, jika ada TextView lain dengan teks
        // yang sama):
        // By orderReceivedTitleXPath =
        // By.xpath("//*[@resource-id='com.example.bengkelgo:id/ID_PARENT_DARI_JUDUL']//android.widget.TextView[@text='Order
        // Received!']");

        try {
            WebElement orderReceivedTitle = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(orderReceivedTitleXPath));
            Assert.assertTrue(orderReceivedTitle.isDisplayed(), "Judul 'Order Received!' tidak muncul.");
            // Teks sudah diverifikasi oleh XPath, tapi bisa juga di-assert lagi jika mau:
            // Assert.assertEquals(orderReceivedTitle.getText(), "Order Received!", "Teks
            // judul konfirmasi tidak sesuai.");
            System.out.println("Judul 'Order Received!' terverifikasi.");

            WebElement backToHomeButton = driver.findElement(BACK_TO_HOME_BUTTON_CONFIRMATION);
            Assert.assertTrue(backToHomeButton.isDisplayed() && backToHomeButton.isEnabled(),
                    "Tombol 'Back to Home' tidak muncul atau tidak aktif.");
            System.out.println("Tombol 'Back to Home' terverifikasi.");

        } catch (Exception e) {
            System.err.println("GAGAL memverifikasi halaman Order Confirmation: " + e.getMessage());
            // Page source sudah dicetak di atas
            e.printStackTrace();
            Assert.fail("Gagal memverifikasi halaman Order Confirmation. Cek page source dan locator.", e);
        }

        System.out.println("Tes testSuccessfulVehicleServiceOrder selesai dengan sukses.");
    }

    /**
     * Metode tearDown dijalankan setelah setiap metode tes.
     */
    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Sesi Appium driver dihentikan untuk VehicleServiceOrderTests.");
        }
    }
}