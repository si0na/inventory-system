package com.example.inventorysystem.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".inventory-item")));

        // Capture the initial list of inventory items
        List<WebElement> inventoryItemsBefore = driver.findElements(By.cssSelector(".inventory-item"));
        int initialCount = inventoryItemsBefore.size();
        assertTrue(initialCount > 0, "No inventory items found to delete.");

        // Find and click the delete button of the first inventory item
        WebElement firstInventoryItem = inventoryItemsBefore.get(0);
        WebElement deleteButton = firstInventoryItem.findElement(By.xpath(".//button[text()='Delete']"));
        deleteButton.click();

        // Wait for the DOM to update and refetch the list of inventory items
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector(".inventory-item"), initialCount - 1));

        // Refetch the inventory items after the DOM update
        List<WebElement> inventoryItemsAfter = driver.findElements(By.cssSelector(".inventory-item"));

        // Verify the number of items decreased by 1
        assertEquals(initialCount - 1, inventoryItemsAfter.size(), "Product not deleted successfully.");
    }

    @Test
    @Order(4)
    public void testNavigationToAnalysisPage() {
        driver.get("http://localhost:3000");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        try {
            // Wait for the "ANALYSIS" link to become clickable
            WebElement analysisLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("ANALYSIS")));
            analysisLink.click();

            // Wait for navigation to the Analysis page
            wait.until(ExpectedConditions.urlToBe("http://localhost:3000/analysis"));

            // Verify navigation
            assertEquals("http://localhost:3000/analysis", driver.getCurrentUrl(), "Failed to navigate to the ANALYSIS page.");

        } catch (TimeoutException e) {
            // Log timeout details
            System.out.println("Timeout occurred: " + e.getMessage());

            // Capture screenshot for debugging
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                Files.copy(screenshot.toPath(), Paths.get("test-failure.png"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            fail("Test failed due to timeout.");
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit(); // Close the browser
        }
    }
}


