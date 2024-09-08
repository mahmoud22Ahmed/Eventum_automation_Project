package Tests;

import DriverFactory.DriverFactory;
import GeneralClasses.General;
import Pages.LoginPage;
import Utilities.DataUtil;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.Duration;

public class LogoutTC {
    private final String ValidUsername = (String) DataUtil.getJsonData("ValidLoginData", "username");
    private final String ValidPassword = (String) DataUtil.getJsonData("ValidLoginData", "password");
    private final String LoginPage_URL = DataUtil.getPropertyValue("Environment", "LOGIN_URL");

    private final String Browser = DataUtil.getPropertyValue("Environment", "BROWSER");

    public LogoutTC() throws IOException {
    }

    @BeforeClass(alwaysRun = true)
    private void Start() throws IOException {
        DriverFactory.setupDriver(Browser);
        DriverFactory.getDriver().manage().window().maximize();
        DriverFactory.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        DriverFactory.getDriver().get(LoginPage_URL);
        new LoginPage(DriverFactory.getDriver()).enterUsername(ValidUsername)
                .enterPassword(ValidPassword).clickLogin();

    }


    @BeforeMethod(alwaysRun = true)
    private void Setup() throws IOException {
        General.getLandingPage(DriverFactory.getDriver());
    }

    /*
      @Title : Check logout functionality
      @Steps:
          1) Log in with valid credentials.
          2) Click on the Welcome menu.
          3) Click on the logout button.
      @Expected result : The user should be redirected back to the login page.
    */
    @Test(groups = {"Valid"}, priority = 1)
    private void TC1_CheckLogout() throws InterruptedException, IOException {
        boolean LogOut;
         new Pages.LandingPage(DriverFactory.getDriver()).pressOnLogout();
        LogOut =  General.checkCurrentURL(DriverFactory.getDriver()
                                       ,LoginPage_URL);
        Assert.assertTrue(LogOut);
    }

    @AfterClass(alwaysRun = true)
    private void tearout() {
        DriverFactory.quitDriver();
    }
}
