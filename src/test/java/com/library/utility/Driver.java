package com.library.utility;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
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
                        String gridAddress = "52.90.101.17";
                        URL url = new URL("http://" + gridAddress + ":4444/wd/hub");
                        DesiredCapabilities chromeCaps = new DesiredCapabilities();
                        chromeCaps.setBrowserName("chrome");
                        driverPool.set(new RemoteWebDriver(url, chromeCaps));
                        break;

                    case "remote-firefox":
                        gridAddress = "52.90.101.17";
                        url = new URL("http://" + gridAddress + ":4444/wd/hub");
                        DesiredCapabilities firefoxCaps = new DesiredCapabilities();
                        firefoxCaps.setBrowserName("firefox");
                        driverPool.set(new RemoteWebDriver(url, firefoxCaps));
                        break;

                    case "chrome":
                        WebDriverManager.chromedriver().setup();
                        ChromeOptions chromeOptions = new ChromeOptions();

                        // Jenkins için headless mode
                        if (isCI()) {
                            chromeOptions.addArguments("--headless");
                            chromeOptions.addArguments("--no-sandbox");
                            chromeOptions.addArguments("--disable-dev-shm-usage");
                            chromeOptions.addArguments("--disable-gpu");
                        }

                        driverPool.set(new ChromeDriver(chromeOptions));
                        break;

                    case "firefox":
                        WebDriverManager.firefoxdriver().setup();
                        FirefoxOptions firefoxOptions = new FirefoxOptions();

                        if (isCI()) {
                            firefoxOptions.addArguments("--headless");
                        }

                        driverPool.set(new FirefoxDriver(firefoxOptions));
                        break;

                    default:
                        throw new RuntimeException("Unknown browser type: " + browserName);
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to initialize WebDriver for: " + browserName, e);
            }
        }

        return driverPool.get();
    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }

    private static boolean isCI() {
        return System.getenv("JENKINS_HOME") != null || System.getenv("CI") != null;
    }
}
