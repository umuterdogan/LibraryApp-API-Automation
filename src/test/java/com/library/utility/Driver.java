package com.library.utility;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class Driver {

    private Driver() {}

    private static InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    public static WebDriver getDriver() {
        if (driverPool.get() == null) {

            String browserName = System.getProperty("browser") != null
                    ? System.getProperty("browser")
                    : ConfigurationReader.getProperty("browser");

            try {
                switch (browserName) {
                    case "remote-chrome":
                        String gridAddress = "**.**.***.**";
                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                        ChromeOptions remoteChrome = new ChromeOptions();
                        remoteChrome.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                        remoteChrome.addArguments("--window-size=1920,1080");
                        driverPool.set(new RemoteWebDriver(url, remoteChrome));
                        break;

                    case "remote-firefox":
                        String gridAddressFirefox = "**.**.***.**";
                        URL urlFirefox = new URL("http://" + gridAddressFirefox + ":4444/wd/hub");
                        FirefoxOptions remoteFirefox = new FirefoxOptions();
                        remoteFirefox.addArguments("--headless");
                        remoteFirefox.addArguments("--width=1920");
                        remoteFirefox.addArguments("--height=1080");
                        driverPool.set(new RemoteWebDriver(urlFirefox, remoteFirefox));
                        break;

                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        driverPool.set(new ChromeDriver());
                        break;

                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        driverPool.set(new FirefoxDriver());
                        break;

                    case "headless-chrome":
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions headlessChrome = new ChromeOptions();
                        headlessChrome.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
                        headlessChrome.addArguments("--window-size=1920,1080");
                        driverPool.set(new ChromeDriver(headlessChrome));
                        break;

                    case "headless-firefox":
                        WebDriverManager.firefoxdriver().setup();
                        FirefoxOptions headlessFirefox = new FirefoxOptions();
                        headlessFirefox.addArguments("--headless");
                        headlessFirefox.addArguments("--width=1920");
                        headlessFirefox.addArguments("--height=1080");
                        driverPool.set(new FirefoxDriver(headlessFirefox));
                        break;

                    default:
                        throw new RuntimeException("Browser type not supported: " + browserName);
                }
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize WebDriver for: " + browserName, e);
            }
        }

        return driverPool.get();
    }

    public static void closeDriver() {
        try {
            if (driverPool.get() != null) {
                driverPool.get().quit();
                driverPool.remove();
            }
        } catch (Exception e) {
            System.out.println("An error occurred while closing the driver: " + e.getMessage());
        }
    }
}
