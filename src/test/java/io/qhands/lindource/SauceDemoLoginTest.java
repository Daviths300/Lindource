package io.qhands.lindource;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class SauceDemoLoginTest {

    private WebDriver driver;

    private static final String LT_USERNAME = "gvasaliadavit3";
    private static final String LT_ACCESS_KEY = "LT_ovoizF7Dufkr9x0ibhLbc4ZWOKe8sELDEqO44LFhXDdFuCW";
    private static final String GRID_URL = "@hub.lambdatest.com/wd/hub";

    @BeforeMethod
    public void setUp() throws MalformedURLException {
        HashMap<String, Object> ltOptions = new HashMap<>();
        ltOptions.put("username", LT_USERNAME);
        ltOptions.put("accessKey", LT_ACCESS_KEY);
        ltOptions.put("build", "QHANDS-01-Practice");
        ltOptions.put("project", "SauceDemo-LambdaTest");
        ltOptions.put("browserName", "Chrome");
        ltOptions.put("browserVersion", "latest");
        ltOptions.put("platformName", "Windows 10");
        ltOptions.put("visual", true);
        ltOptions.put("video", true);

        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("LT:Options", ltOptions);

        driver = new RemoteWebDriver(
                new URL("https://" + LT_USERNAME + ":" + LT_ACCESS_KEY + GRID_URL),
                caps
        );
    }

    @Test
    public void testValidLogin() {
        driver.get("https://www.saucedemo.com");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        Assert.assertTrue(driver.getCurrentUrl().contains("inventory"),
                "Login failed - inventory page not loaded");
    }

    @Test
    public void testInvalidLogin() {
        driver.get("https://www.saucedemo.com");
        driver.findElement(By.id("user-name")).sendKeys("wrong_user");
        driver.findElement(By.id("password")).sendKeys("wrong_pass");
        driver.findElement(By.id("login-button")).click();
        String errorMessage = driver.findElement(
                By.cssSelector("[data-test='error']")).getText();
        Assert.assertTrue(errorMessage.contains("Username and password do not match"),
                "Error message not displayed correctly");
    }

    @Test
    public void testProductPageLoads() {
        driver.get("https://www.saucedemo.com");
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        String pageTitle = driver.findElement(
                By.cssSelector(".title")).getText();
        Assert.assertEquals(pageTitle, "Products",
                "Product page title is incorrect");
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}