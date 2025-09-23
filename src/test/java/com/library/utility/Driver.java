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

    private Driver() {
    }

    private static InheritableThreadLocal<WebDriver> driverPool = new InheritableThreadLocal<>();

    public static WebDriver getDriver() {

        if (driverPool.get() == null) {
            String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : ConfigurationReader.getProperty("browser");

            switch(browserName){
                case "remote-chrome":
                    try {

                        String gridAddress = "52.90.101.17";
                        URL url = new URL("http://"+ gridAddress + ":4444/wd/hub");
                        ChromeOptions chromeOptions = new ChromeOptions();
                        chromeOptions.addArguments("--start-maximized");
                        driverPool.set(new RemoteWebDriver(url, chromeOptions));
                        //driverPool.set(new RemoteWebDriver(new URL("http://0.0.0.0:4444/wd/hub"),desiredCapabilities));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "remote-firefox":
                    try {

                        String gridAddress = "52.90.101.17";
                        URL url = new URL("http://"+ gridAddress + ":4444/wd/hub");
                        FirefoxOptions firefoxOptions=new FirefoxOptions();
                        firefoxOptions.addArguments("--start-maximized");
                        driverPool.set(new RemoteWebDriver(url, firefoxOptions));
                        //driverPool.set(new RemoteWebDriver(new URL("http://0.0.0.0:4444/wd/hub"),desiredCapabilities));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driverPool.set(new ChromeDriver());
                    driverPool.get().get(ConfigurationReader.getProperty("library_url"));
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driverPool.set(new FirefoxDriver());
                    driverPool.get().get(ConfigurationReader.getProperty("library_url"));
                    break;
            }

        }

        return driverPool.get();

    }


    public static void closeDriver(){
        try {
            if(driverPool.get() != null){
                driverPool.get().quit();
                driverPool.remove();
            }
        } catch (Exception e) {
            System.out.println("An error occurred while closing the driver: " + e.getMessage());
        }
    }
}
