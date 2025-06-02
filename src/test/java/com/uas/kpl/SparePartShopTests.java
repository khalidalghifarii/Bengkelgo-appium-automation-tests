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
import java.util.List;

public class SparePartShopTests {

    private AndroidDriver driver;
    private WebDriverWait wait;

    // --- Konstanta Locator ---
    // MainActivity
    private static final By SPARE_PART_SHOP_CARD = By.id("com.example.bengkelgo:id/cardSparePartShop");
    private static final By USERNAME_TEXT_VIEW_MAIN_FOR_LOGIN_CHECK = By.id("com.example.bengkelgo:id/tvUsername");

    // SparePartListActivity
    private static final By CHIP_RIMS = By.id("com.example.bengkelgo:id/chipRims");
    private static final By TV_SEE_ALL_FEATURED = By.id("com.example.bengkelgo:id/tvSeeAll");
    // Locator untuk item produk di RecyclerView. Ini mungkin perlu XPath yang lebih
    // spesifik.
    // Contoh jika produk pertama adalah yang kita inginkan dan ada di rvSpareParts
    // (featured list)
    // dan itemnya menggunakan layout item_spare_part_featured.xml
    private static final String TARGET_PRODUCT_NAME = "BBS F1-R Diamond Black"; // Atau nama produk target Anda
    // XPath untuk menemukan produk berdasarkan namanya di dalam RecyclerView.
    // Ini perlu disesuaikan berdasarkan struktur item RecyclerView Anda (misalnya,
    // tvFeaturedPartName atau tvGridPartName).
    // Contoh untuk tvFeaturedPartName:
    private static final By PRODUCT_ITEM_BY_NAME_XPATH = By.xpath(String.format(
            "//android.widget.TextView[@resource-id='com.example.bengkelgo:id/tvFeaturedPartName' and @text='%s']/ancestor::com.google.android.material.card.MaterialCardView",
            TARGET_PRODUCT_NAME));
    // Atau jika lebih sederhana dan produk ada di rvGridProducts dengan
    // tvGridPartName:
    // private static final By PRODUCT_ITEM_BY_NAME_XPATH =
    // By.xpath(String.format("//android.widget.TextView[@resource-id='com.example.bengkelgo:id/tvGridPartName'
    // and @text='%s']/ancestor::com.google.android.material.card.MaterialCardView",
    // TARGET_PRODUCT_NAME));

    // SparePartDetailActivity
    private static final By PRODUCT_DETAIL_NAME = By.id("com.example.bengkelgo:id/tvPartDetailName");
    private static final By TAB_LAYOUT_DETAIL = By.id("com.example.bengkelgo:id/tabLayoutDetail");
    // XPath untuk Tab Items
    private static final By TAB_OVERVIEW = By.xpath(
            "//android.widget.LinearLayout[@content-desc='Overview']//android.widget.TextView[@text='Overview']");
    // Atau jika content-desc tidak ada: //android.widget.TextView[@text='Overview']
    private static final By TAB_FEATURES = By.xpath(
            "//android.widget.LinearLayout[@content-desc='Features']//android.widget.TextView[@text='Features']");
    private static final By TAB_SPECIFICATIONS = By.xpath(
            "//android.widget.LinearLayout[@content-desc='Specifications']//android.widget.TextView[@text='Specifications']");

    private static final By OVERVIEW_CONTENT = By.id("com.example.bengkelgo:id/tvPartDetailOverview");
    private static final By FEATURES_CONTENT_LAYOUT = By.id("com.example.bengkelgo:id/layoutPartDetailFeatures");
    private static final By SPECIFICATIONS_CONTENT_LAYOUT = By
            .id("com.example.bengkelgo:id/layoutPartDetailSpecifications");
    private static final By ADD_TO_CART_BUTTON = By.id("com.example.bengkelgo:id/btnAddToCart");

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
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        System.out.println("Appium driver initialized for SparePartShopTests.");

        try {
            System.out.println("Mengecek status login...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_TEXT_VIEW_MAIN_FOR_LOGIN_CHECK));
            System.out.println("Pengguna sudah login, berada di MainActivity.");
        } catch (Exception e) {
            System.out.println("Pengguna belum login. Mencoba login...");
            performLogin("user@gmail.com", "user123"); // Ganti kredensial
            wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME_TEXT_VIEW_MAIN_FOR_LOGIN_CHECK));
            System.out.println("Login berhasil, berada di MainActivity.");
        }
    }

    private void performLogin(String email, String password) {
        // ... (Metode login yang sama seperti di kelas lain) ...
        WebDriverWait loginWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement emailField = loginWait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("com.example.bengkelgo:id/edtEmail")));
        emailField.sendKeys(email);
        driver.findElement(By.id("com.example.bengkelgo:id/edtPassword")).sendKeys(password);
        driver.findElement(By.id("com.example.bengkelgo:id/btnLogin")).click();
    }

    @Test(description = "Test Case: Menjelajahi daftar spare part, melihat detail, dan menambah ke keranjang.")
    public void testBrowseAndAddToCartSparePart() throws InterruptedException {
        System.out.println("Memulai tes: testBrowseAndAddToCartSparePart...");

        // 1. Dari MainActivity, klik card "Spare Part Shop"
        System.out.println("Mengklik card Spare Part Shop...");
        WebElement sparePartShopCard = wait.until(ExpectedConditions.elementToBeClickable(SPARE_PART_SHOP_CARD));
        sparePartShopCard.click();
        System.out.println("Card Spare Part Shop diklik. Menunggu SparePartListActivity...");

        // 2. Di SparePartListActivity
        // Verifikasi chip "Rims" aktif (atau tunggu elemen lain yang menandakan halaman
        // termuat)
        WebElement chipRims = wait.until(ExpectedConditions.visibilityOfElementLocated(CHIP_RIMS));
        // Ambil atribut "checked" dari chip. Nilainya akan berupa String "true" atau
        // "false".
        String chipRimsCheckedState = chipRims.getAttribute("checked");

        Assert.assertTrue(chipRims.isDisplayed() && "true".equalsIgnoreCase(chipRimsCheckedState),
                "Chip Rims tidak aktif secara default. Status 'checked': " + chipRimsCheckedState);
        System.out.println("SparePartListActivity termuat, Chip Rims aktif (checked: " + chipRimsCheckedState + ").");

        // Klik "See All"
        WebElement tvSeeAll = wait.until(ExpectedConditions.elementToBeClickable(TV_SEE_ALL_FEATURED));
        tvSeeAll.click();
        System.out.println("Tombol 'See All' diklik.");

        // Tunggu dan klik area "Shop Now" dari produk target
        System.out.println("Mencari area 'Shop Now' untuk produk: " + TARGET_PRODUCT_NAME);

        // XPath untuk menemukan LinearLayout "Shop Now" di dalam card produk featured
        // yang memiliki TextView nama produk (tvFeaturedPartName) yang sesuai.
        // XPath untuk menemukan MaterialCardView yang berisi TextView nama produk yang
        // sesuai.
        By productCardLocator = By.xpath(
                String.format(
                        "//android.widget.TextView[@resource-id='com.example.bengkelgo:id/tvFeaturedPartName' and @text='%s']/ancestor::androidx.cardview.widget.CardView[1]",
                        TARGET_PRODUCT_NAME) // Menggunakan androidx.cardview.widget.CardView
        );

        try {
            WebElement productCard = wait.until(ExpectedConditions.elementToBeClickable(productCardLocator));
            System.out.println("Card produk '" + TARGET_PRODUCT_NAME + "' ditemukan. Mencoba mengklik card...");
            productCard.click();
            System.out.println("Card produk '" + TARGET_PRODUCT_NAME + "' diklik. Menunggu SparePartDetailActivity...");
        } catch (Exception e) {
            System.err.println("GAGAL mengklik card produk '" + TARGET_PRODUCT_NAME + "'. Error: " + e.getMessage());
            System.err.println("PAGE SOURCE saat error klik card:\n" + driver.getPageSource());
            e.printStackTrace();
            Assert.fail("Gagal mengklik card produk '" + TARGET_PRODUCT_NAME + "'.", e);
        }

        // 3. Di SparePartDetailActivity
        // Verifikasi nama produk
        WebElement productDetailName = wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_DETAIL_NAME));
        Assert.assertTrue(productDetailName.getText().contains(TARGET_PRODUCT_NAME),
                "Nama produk di halaman detail tidak sesuai.");
        System.out.println("SparePartDetailActivity termuat. Nama produk: " + productDetailName.getText());

        // Verifikasi tab Overview (default)
        WebElement overviewContent = wait.until(ExpectedConditions.visibilityOfElementLocated(OVERVIEW_CONTENT));
        Assert.assertTrue(overviewContent.isDisplayed(), "Konten Overview tidak terlihat.");
        System.out.println("Tab Overview aktif dan konten terlihat.");

        // Klik dan verifikasi tab Features
        // Locator untuk tab bisa lebih kompleks. Cek dengan Inspector.
        // Asumsi TextView di dalam LinearLayout dengan content-desc.
        System.out.println("Mengklik tab Features...");
        WebElement tabFeatures = wait.until(ExpectedConditions.elementToBeClickable(TAB_FEATURES));
        tabFeatures.click();
        Thread.sleep(500); // Jeda singkat agar konten features termuat
        WebElement featuresContent = wait.until(ExpectedConditions.visibilityOfElementLocated(FEATURES_CONTENT_LAYOUT));
        Assert.assertTrue(featuresContent.isDisplayed(), "Konten Features tidak terlihat setelah klik tab.");
        System.out.println("Tab Features diklik dan konten terlihat.");

        // Klik dan verifikasi tab Specifications
        System.out.println("Mengklik tab Specifications...");
        WebElement tabSpecifications = wait.until(ExpectedConditions.elementToBeClickable(TAB_SPECIFICATIONS));
        tabSpecifications.click();
        Thread.sleep(500); // Jeda singkat
        WebElement specificationsContent = wait
                .until(ExpectedConditions.visibilityOfElementLocated(SPECIFICATIONS_CONTENT_LAYOUT));
        Assert.assertTrue(specificationsContent.isDisplayed(),
                "Konten Specifications tidak terlihat setelah klik tab.");
        System.out.println("Tab Specifications diklik dan konten terlihat.");

        // Kembali ke tab Overview sebelum Add to Cart (jika perlu, atau pastikan tombol
        // Add to Cart selalu visible)
        // WebElement tabOverviewAgain =
        // wait.until(ExpectedConditions.elementToBeClickable(TAB_OVERVIEW));
        // tabOverviewAgain.click();
        // Thread.sleep(500);

        // Klik "Add To Cart"
        System.out.println("Mengklik tombol Add To Cart...");
        WebElement addToCartButton = wait.until(ExpectedConditions.elementToBeClickable(ADD_TO_CART_BUTTON));
        addToCartButton.click();
        System.out.println("Tombol Add To Cart diklik.");

        // Verifikasi Toast (SULIT, bisa di-skip atau observasi manual)
        // Contoh cara MUNGKIN menangkap Toast (membutuhkan Appium versi tertentu dan
        // keberuntungan):
        // try {
        // WebElement toastMessage =
        // wait.until(ExpectedConditions.visibilityOfElementLocated(
        // By.xpath("//android.widget.Toast[1]"))); // XPath umum untuk Toast
        // Assert.assertTrue(toastMessage.getText().contains("added to cart"), "Pesan
        // toast tidak sesuai.");
        // System.out.println("Pesan toast terverifikasi: " + toastMessage.getText());
        // } catch (Exception e) {
        // System.out.println("Tidak dapat memverifikasi pesan toast secara otomatis: "
        // + e.getMessage());
        // }

        // Untuk sekarang, kita anggap klik Add to Cart berhasil jika tidak ada error.
        System.out.println("Tes testBrowseAndAddToCartSparePart selesai.");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            System.out.println("Sesi Appium driver dihentikan untuk SparePartShopTests.");
        }
    }
}