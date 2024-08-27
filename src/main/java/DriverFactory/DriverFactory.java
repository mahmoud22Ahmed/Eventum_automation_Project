package DriverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DriverFactory {
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static void setupDriver(String browser) //Edge edge EDGE
    {
        switch (browser.toLowerCase())
        {
            case "chrome":
                driverThreadLocal.set(new ChromeDriver());
                break;
            case "firefox":
                driverThreadLocal.set(new FirefoxDriver());
                break;
            default:

                driverThreadLocal.set(new EdgeDriver());
        }

    }
    public static WebDriver getDriver()
    {
        return driverThreadLocal.get();
    }
    public static void quitDriver()
    {
        getDriver().quit();
        driverThreadLocal.remove();
    }
}