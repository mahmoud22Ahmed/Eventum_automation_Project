package Tests;

import DriverFactory.DriverFactory;
import Utilities.DataUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Driver;
import java.util.PrimitiveIterator;

public class LoginTC {
    //Test Cases Data
    private final String Username = DataUtil.getJsonData("ValidLoginData","username");
    private final String password = DataUtil.getJsonData("ValidLoginData","password");
    private final String LoginPage_URL = DataUtil.getPropertyValue("Environment","LOGIN_URL");
    private final String Browser = DataUtil.getPropertyValue("Environment","BROWSER");

    public LoginTC() throws IOException {
    }


    @BeforeMethod(alwaysRun = true)
    private void Setup(){

        //chromeDriver.get(LoginPage_URL);
        DriverFactory.setupDriver(Browser);
        DriverFactory.getDriver().get(LoginPage_URL);


    }

    @Test
    private void TC1(){
        
    }

    @Test
    private void TC2(){
        
    }

    @AfterMethod(alwaysRun = true)
    private void tearout(){
        DriverFactory.quitDriver();
    }
}
