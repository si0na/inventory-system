package com.example.inventorysystem.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeleniumTest {

    private WebDriver driver;
    private final String baseURL = "http://localhost:3000"; // React app URL

    @BeforeAll
    public void setUpSuite() {
        WebDriverManager.chromedriver().setup(); // Setup WebDriver
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10)); // Implicit wait
        driver.manage().window().maximize();
    }

    @Test
    @Order(1)
    public void testAddProductForm() {
        driver.get(baseURL);
        
        WebElement nameInput = driver.findElement(By.cssSelector("input[placeholder='Name']"));
        WebElement quantityInput = driver.findElement(By.cssSelector("input[placeholder='Quantity']"));
        WebElement priceInput = driver.findElement(By.cssSelector("input[placeholder='Price']"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        // Fill in the form
        nameInput.sendKeys("Test Product");
        quantityInput.sendKeys("10");
        priceInput.sendKeys("100.0");
        
        // Submit the form
        submitButton.click();

        // Wait for the product to appear in the inventory list
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".inventory-item")));

        // Verify the product appears in the inventory list
        List<WebElement> inventoryItems = driver.findElements(By.cssSelector(".inventory-item"));
        assertTrue(inventoryItems.size() > 0, "Product not added to inventory.");
    }

    @Test
    @Order(2)
    public void testEditProduct() {
        driver.get(baseURL);

        WebElement editButton = driver.findElement(By.xpath("//button[text()='Edit']"));
        editButton.click();

        WebElement nameInput = driver.findElement(By.cssSelector("input[placeholder='Name']"));
        WebElement quantityInput = driver.findElement(By.cssSelector("input[placeholder='Quantity']"));
        WebElement priceInput = driver.findElement(By.cssSelector("input[placeholder='Price']"));
        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));

        // Modify the product details
        nameInput.clear();
        nameInput.sendKeys("Updated Product");
        quantityInput.clear();
        quantityInput.sendKeys("20");
        priceInput.clear();
        priceInput.sendKeys("150.0");

        // Submit the form
        submitButton.click();

        // Wait for the updated product to appear
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//h3[text()='Updated Product']"), "Updated Product"));

        // Verify the updated product appears in the inventory list
        WebElement updatedProduct = driver.findElement(By.xpath("//h3[text()='Updated Product']"));
        assertNotNull(updatedProduct, "Product not updated correctly.");
    }
    @Test
    @Order(3)
    public void testDeleteProduct() {
        driver.get(baseURL);

        // Wait for the inventory items to load
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Delete']")));

        // Find the delete button for the first item
        WebElement deleteButton = driver.findElement(By.xpath("//button[text()='Delete']"));
        deleteButton.click();

        // Wait for the delete button to be removed from the DOM
        wait.until(ExpectedConditions.stalenessOf(deleteButton));

        // Verify the product has been deleted
        List<WebElement> inventoryItems = driver.findElements(By.cssSelector(".inventory-item"));
        assertTrue(inventoryItems.isEmpty(), "Product not deleted.");
    }




    @Test
    @Order(4)
    public void testNavigationAndRouting() {
        driver.get(baseURL);
        WebElement analysisLink = driver.findElement(By.linkText("Analysis"));
        analysisLink.click();

        // Wait for the URL to contain "/analysis"
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/analysis"));

        String url = driver.getCurrentUrl();
        assertTrue(url.endsWith("/analysis"), "Navigation to Analysis page failed.");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Close the browser
        }
    }
}

